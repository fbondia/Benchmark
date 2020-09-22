mvn clean package cargo:run -Pbugscout-execute \
-Dbugscout.agent.location=/Users/fbondia/Source/Bugscout/iast/jvm/bug-iast-jvm/agent/target/bugscout-iast-agent-1.0.1-SNAPSHOT.jar \
-Dbugscout.url=https://bugscout.iast/bugscout \
-Dbugscout.source=FIRST-TEST \
-Dbugscout.projectKey=first-test \
-Dbugscout.username=elastic \
-Dbugscout.password=changeme \
-Dbugscout.rules=

echo "Benchmark available at https://localhost:8443/benchmark/"

