# Test script for the SaaS API

# Base URL
$baseUrl = "http://localhost:8080"

# Test the auto pricing endpoint
Write-Host "Testing Auto Pricing API..." -ForegroundColor Green
$vehicleId = "00000000-0000-0000-0000-000000000001" # Replace with a valid vehicle ID from your database
$organizationId = "00000000-0000-0000-0000-000000000001" # Replace with a valid organization ID from your database
$coverageType = "THIRD_PARTY"
$bonusMalusCoefficient = 1.0

try
{
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auto/pricing/base-premium?vehicleId=$vehicleId&organizationId=$organizationId" -Method GET -ErrorAction Stop
    Write-Host "Base Premium API Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json
}
catch
{
    Write-Host "Error calling Base Premium API: $_" -ForegroundColor Red
}

try
{
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auto/pricing/final-premium?vehicleId=$vehicleId&coverageType=$coverageType&bonusMalusCoefficient=$bonusMalusCoefficient&organizationId=$organizationId" -Method GET -ErrorAction Stop
    Write-Host "Final Premium API Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json
}
catch
{
    Write-Host "Error calling Final Premium API: $_" -ForegroundColor Red
}

# Test the bonus-malus endpoint
Write-Host "Testing Bonus-Malus API..." -ForegroundColor Green
$customerId = "00000000-0000-0000-0000-000000000001" # Replace with a valid customer ID from your database
$claimCount = 0

try
{
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auto/bonus-malus/$customerId/calculate?claimCount=$claimCount&organizationId=$organizationId" -Method POST -ErrorAction Stop
    Write-Host "Bonus-Malus API Response:" -ForegroundColor Cyan
    $response | ConvertTo-Json
}
catch
{
    Write-Host "Error calling Bonus-Malus API: $_" -ForegroundColor Red
}

Write-Host "Tests completed." -ForegroundColor Green
