# Environment
- Java 21
- gradle 8.14.3
- allure report 2.34
- docker
# Github
Self hosted agent is used to execute the workflows.
# Running tests
  1. Manually from IDE
     - set gradle test runner to intelij so it uses testng
  2. FatJar
      - ./gradlew clean fatJar
     - java -jar Avenga-all-1.0-SNAPSHOT-tests.jar -testjar Avenga-all-1.0-SNAPSHOT-tests.jar -xmlpathinjar suite.xml
  3. Workflow with fatJar
     - run Java CI with Gradle workflow
     - report can be found in the artifacts of the pipeline
  4. Docker
     - docker build --no-cache -t avenga .
     - docker run -v "{path in local}\allure-report:/app/allure-report" avenga:latest
  5. Workflow with docker
