export SONAR_TOKEN=$(cat ~/.sonar/sonar-global)

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=fibonacci-task-manager \
  -Dsonar.projectName='fibonacci-task-manager' \
  -Dsonar.host.url=http://localhost:9000
