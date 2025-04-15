# Codebase Indexation Script
# This script scans the Java codebase and generates a comprehensive index

$outputDir = "doc/codebase-index"
$srcDir = "src/main/java"
$resourcesDir = "src/main/resources"

# Create output directory if it doesn't exist
if (-not (Test-Path $outputDir))
{
    New-Item -ItemType Directory -Path $outputDir | Out-Null
    Write-Host "Created output directory: $outputDir"
}

# Function to get all Java files
function Get-JavaFiles
{
    param (
        [string]$directory
    )

    return Get-ChildItem -Path $directory -Filter "*.java" -Recurse
}

# Function to extract package information
function Get-PackageStructure
{
    param (
        [string]$srcDirectory
    )

    $packages = @{ }
    $javaFiles = Get-JavaFiles -directory $srcDirectory

    foreach ($file in $javaFiles)
    {
        $content = Get-Content $file.FullName -Raw
        if ($content -match "package\s+([\w\.]+);")
        {
            $package = $matches[1]
            if (-not $packages.ContainsKey($package))
            {
                $packages[$package] = @()
            }
            $packages[$package] += $file.Name.Replace(".java", "")
        }
    }

    return $packages
}

# Function to extract class information
function Get-ClassInfo
{
    param (
        [string]$filePath
    )

    $content = Get-Content $filePath -Raw
    $className = ""
    $methods = @()
    $annotations = @()
    $extends = ""
    $implements = @()

    # Extract class name
    if ($content -match "(?:public|private|protected)?\s+(?:abstract|final)?\s+class\s+(\w+)")
    {
        $className = $matches[1]
    }
    elseif ($content -match "(?:public|private|protected)?\s+(?:abstract|final)?\s+interface\s+(\w+)")
    {
        $className = $matches[1]
    }
    elseif ($content -match "(?:public|private|protected)?\s+(?:abstract|final)?\s+enum\s+(\w+)")
    {
        $className = $matches[1]
    }

    # Extract extends
    if ($content -match "class\s+\w+\s+extends\s+(\w+)")
    {
        $extends = $matches[1]
    }

    # Extract implements
    if ($content -match "(?:class|interface)\s+\w+(?:\s+extends\s+\w+)?\s+implements\s+([\w\s,]+)")
    {
        $implementsStr = $matches[1]
        $implements = $implementsStr -split "," | ForEach-Object { $_.Trim() }
    }

    # Extract methods
    $methodMatches = [regex]::Matches($content, "(?:public|private|protected)(?:\s+static|\s+final|\s+abstract)*\s+(?:<.*>)?\s*(?:[\w\[\]<>]+)\s+(\w+)\s*\([^)]*\)")
    foreach ($match in $methodMatches)
    {
        $methods += $match.Groups[1].Value
    }

    # Extract annotations
    $annotationMatches = [regex]::Matches($content, "@(\w+)")
    foreach ($match in $annotationMatches)
    {
        $annotations += $match.Groups[1].Value
    }

    return @{
        ClassName = $className
        Methods = $methods
        Annotations = $annotations
        Extends = $extends
        Implements = $implements
    }
}

# Function to extract REST endpoints
function Get-RestEndpoints
{
    param (
        [string]$srcDirectory
    )

    $endpoints = @()
    $javaFiles = Get-JavaFiles -directory $srcDirectory

    foreach ($file in $javaFiles)
    {
        $content = Get-Content $file.FullName -Raw

        # Check if it's a controller
        if ($content -match "@(?:Rest)?Controller")
        {
            $classInfo = Get-ClassInfo -filePath $file.FullName
            $className = $classInfo.ClassName

            # Extract class-level mapping
            $baseMapping = ""
            if ($content -match "@RequestMapping\s*\(\s*(?:value\s*=)?\s*[""']([^""']*)[""']")
            {
                $baseMapping = $matches[1]
            }

            # Extract method-level mappings
            $methodMappings = [regex]::Matches($content, "@(?:GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)\s*\(\s*(?:value\s*=)?\s*[""']([^""']*)[""']")
            foreach ($mapping in $methodMappings)
            {
                $methodPath = $mapping.Groups[1].Value
                $fullPath = if ($baseMapping)
                {
                    "$baseMapping$methodPath"
                }
                else
                {
                    $methodPath
                }

                # Determine HTTP method
                $httpMethod = "GET"
                if ($mapping.Value -match "PostMapping")
                {
                    $httpMethod = "POST"
                }
                elseif ($mapping.Value -match "PutMapping")
                {
                    $httpMethod = "PUT"
                }
                elseif ($mapping.Value -match "DeleteMapping")
                {
                    $httpMethod = "DELETE"
                }
                elseif ($mapping.Value -match "PatchMapping")
                {
                    $httpMethod = "PATCH"
                }

                $endpoints += [PSCustomObject]@{
                    Path = $fullPath
                    HttpMethod = $httpMethod
                    Controller = $className
                }
            }
        }
    }

    return $endpoints
}

# Function to extract database entities
function Get-DatabaseEntities
{
    param (
        [string]$srcDirectory
    )

    $entities = @()
    $javaFiles = Get-JavaFiles -directory $srcDirectory

    foreach ($file in $javaFiles)
    {
        $content = Get-Content $file.FullName -Raw

        # Check if it's an entity
        if ($content -match "@Entity")
        {
            $classInfo = Get-ClassInfo -filePath $file.FullName
            $className = $classInfo.ClassName

            # Extract table name
            $tableName = $className
            if ($content -match "@Table\s*\(\s*name\s*=\s*[""']([^""']*)[""']")
            {
                $tableName = $matches[1]
            }

            # Extract fields
            $fields = @()
            $fieldMatches = [regex]::Matches($content, "(?:@Column[^;]*)?(?:private|protected|public)\s+([\w<>]+(?:\[\])?)\s+(\w+)\s*;")
            foreach ($match in $fieldMatches)
            {
                $fieldType = $match.Groups[1].Value
                $fieldName = $match.Groups[2].Value
                $fields += [PSCustomObject]@{
                    Name = $fieldName
                    Type = $fieldType
                }
            }

            $entities += [PSCustomObject]@{
                ClassName = $className
                TableName = $tableName
                Fields = $fields
            }
        }
    }

    return $entities
}

# Function to extract i18n messages
function Get-I18nMessages
{
    param (
        [string]$resourcesDir
    )

    $i18nFiles = Get-ChildItem -Path $resourcesDir -Recurse -Filter "*.properties" | Where-Object { $_.Name -match "messages.*\.properties" }
    $messages = @{ }

    foreach ($file in $i18nFiles)
    {
        $locale = "default"
        if ($file.Name -match "messages_([a-z]{2})\.properties")
        {
            $locale = $matches[1]
        }

        $messages[$locale] = @{ }
        $content = Get-Content $file.FullName

        foreach ($line in $content)
        {
            if ($line -match "^\s*([^#][^=]+)=(.*)$")
            {
                $key = $matches[1].Trim()
                $value = $matches[2].Trim()
                $messages[$locale][$key] = $value
            }
        }
    }

    return $messages
}

# Generate index files
function Generate-IndexFiles
{
    param (
        [hashtable]$packages,
        [array]$endpoints,
        [array]$entities,
        [hashtable]$i18nMessages
    )

    # Generate main index file
    $mainIndexContent = @"
# Codebase Index

This document provides a comprehensive index of the SaaS application codebase.

## Project Structure

The project follows a modular architecture with the following main components:

- **common**: Common utilities, configurations, and base components
- **core**: Core business logic and domain models
- **insurance**: Insurance-specific modules and functionality

## Package Overview

Total packages: $( $packages.Count )
Total classes: $( $packages.Values | ForEach-Object { $_.Count } | Measure-Object -Sum | Select-Object -ExpandProperty Sum )

"@

    # Add package information
    $mainIndexContent += @"

## Package Hierarchy

"@

    foreach ($package in $packages.Keys | Sort-Object)
    {
        $packageClasses = $packages[$package]
        $mainIndexContent += @"
- $package ($( $packageClasses.Count ) classes)
"@
    }

    # Save main index file
    $mainIndexContent | Out-File -FilePath "$outputDir/index.md" -Encoding utf8
    Write-Host "Generated main index file: $outputDir/index.md"

    # Generate package details
    foreach ($package in $packages.Keys | Sort-Object)
    {
        $packageClasses = $packages[$package]
        $packageContent = @"
# Package: $package

This package contains $( $packageClasses.Count ) classes.

## Classes

"@

        foreach ($class in $packageClasses | Sort-Object)
        {
            $packageContent += @"
- $class
"@
        }

        # Create package directory if it doesn't exist
        $packageDir = $package -replace "\.", "/"
        $packageOutputDir = "$outputDir/packages/$packageDir"
        if (-not (Test-Path $packageOutputDir))
        {
            New-Item -ItemType Directory -Path $packageOutputDir -Force | Out-Null
        }

        # Save package details file
        $packageContent | Out-File -FilePath "$packageOutputDir/index.md" -Encoding utf8
        Write-Host "Generated package details: $packageOutputDir/index.md"
    }

    # Generate API endpoints documentation
    if ($endpoints.Count -gt 0)
    {
        $endpointsContent = @"
# API Endpoints

This document lists all REST API endpoints in the application.

| HTTP Method | Path | Controller |
|-------------|------|------------|
"@

        foreach ($endpoint in $endpoints | Sort-Object -Property Path)
        {
            $endpointsContent += @"
| $( $endpoint.HttpMethod ) | $( $endpoint.Path ) | $( $endpoint.Controller ) |
"@
        }

        # Save endpoints file
        $endpointsContent | Out-File -FilePath "$outputDir/endpoints.md" -Encoding utf8
        Write-Host "Generated API endpoints documentation: $outputDir/endpoints.md"
    }

    # Generate database entities documentation
    if ($entities.Count -gt 0)
    {
        $entitiesContent = @"
# Database Entities

This document lists all database entities in the application.

"@

        foreach ($entity in $entities | Sort-Object -Property ClassName)
        {
            $entitiesContent += @"
## $( $entity.ClassName )

**Table name:** $( $entity.TableName )

| Field | Type |
|-------|------|
"@

            foreach ($field in $entity.Fields)
            {
                $entitiesContent += @"
| $( $field.Name ) | $( $field.Type ) |
"@
            }

            $entitiesContent += "`n"
        }

        # Save entities file
        $entitiesContent | Out-File -FilePath "$outputDir/entities.md" -Encoding utf8
        Write-Host "Generated database entities documentation: $outputDir/entities.md"
    }

    # Generate i18n messages documentation
    if ($i18nMessages.Count -gt 0)
    {
        $i18nContent = @"
# Internationalization (i18n) Messages

This document lists all internationalization messages in the application.

"@

        foreach ($locale in $i18nMessages.Keys | Sort-Object)
        {
            $localeMessages = $i18nMessages[$locale]
            $i18nContent += @"
## Locale: $locale

Total messages: $( $localeMessages.Count )

| Key | Value |
|-----|-------|
"@

            foreach ($key in $localeMessages.Keys | Sort-Object)
            {
                $value = $localeMessages[$key]
                $i18nContent += @"
| $key | $value |
"@
            }

            $i18nContent += "`n"
        }

        # Save i18n file
        $i18nContent | Out-File -FilePath "$outputDir/i18n.md" -Encoding utf8
        Write-Host "Generated i18n messages documentation: $outputDir/i18n.md"
    }
}

# Main execution
Write-Host "Starting codebase indexation..."

# Get package structure
Write-Host "Extracting package structure..."
$packages = Get-PackageStructure -srcDirectory $srcDir

# Get REST endpoints
Write-Host "Extracting REST endpoints..."
$endpoints = Get-RestEndpoints -srcDirectory $srcDir

# Get database entities
Write-Host "Extracting database entities..."
$entities = Get-DatabaseEntities -srcDirectory $srcDir

# Get i18n messages
Write-Host "Extracting i18n messages..."
$i18nMessages = Get-I18nMessages -resourcesDir $resourcesDir

# Generate index files
Write-Host "Generating index files..."
Generate-IndexFiles -packages $packages -endpoints $endpoints -entities $entities -i18nMessages $i18nMessages

Write-Host "Codebase indexation completed successfully!"
Write-Host "Index files are available in the $outputDir directory."
