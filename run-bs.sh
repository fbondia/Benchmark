mvn clean package cargo:run \
-Dmaven.wagon.http.ssl.insecure=true \
-Dmaven.wagon.http.ssl.allowall=true \
-Dmaven.wagon.http.ssl.ignore.validity.dates=true \
-Pdeploywbugscout

echo "Benchmark available at https://localhost:8443/benchmark/"

