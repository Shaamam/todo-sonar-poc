#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}Todo SonarQube - Coverage Report${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# Clean and run tests with coverage
echo -e "${YELLOW}Running tests with JaCoCo coverage...${NC}"
./gradlew clean test jacocoTestReport

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Tests completed successfully${NC}"
    echo ""
    
    # Show coverage report location
    echo -e "${BLUE}Coverage Reports:${NC}"
    echo "  HTML: build/reports/jacoco/test/html/index.html"
    echo "  XML:  build/reports/jacoco/test/jacocoTestReport.xml"
    echo ""
    
    # Check if coverage report exists and show summary
    if [ -f "build/reports/jacoco/test/html/index.html" ]; then
        echo -e "${GREEN}✓ Coverage report generated${NC}"
        echo ""
        echo -e "${YELLOW}To view the coverage report, open:${NC}"
        echo "  open build/reports/jacoco/test/html/index.html"
        echo ""
    fi
    
    # Instructions for SonarQube
    echo -e "${BLUE}Next Steps for SonarQube Analysis:${NC}"
    echo "1. Start SonarQube server:"
    echo "   docker run -d --name sonarqube -p 9000:9000 sonarqube:latest"
    echo ""
    echo "2. Access SonarQube at http://localhost:9000"
    echo "   Default credentials: admin/admin"
    echo ""
    echo "3. Generate a token from: Administration > Security > Users"
    echo ""
    echo "4. Run SonarQube analysis:"
    echo "   ./gradlew sonar -Dsonar.login=YOUR_TOKEN"
    echo ""
else
    echo -e "${YELLOW}✗ Tests failed. Please fix the issues and try again.${NC}"
    exit 1
fi
