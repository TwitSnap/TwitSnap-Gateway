FILE="app.jar"

# Check if the file exists
if [ -f "$FILE" ]; then
    echo "$FILE exists. Deleting it."
    rm "$FILE"
fi

./gradlew build -x test

mv ./build/libs/TwitSnap-Gateway-0.0.1-SNAPSHOT.jar ./app.jar

java -javaagent:./src/newrelic/newrelic.jar -jar app.jar