FROM openjdk:17-alpine
EXPOSE 8082
COPY target/Restaurant*.jar /restaurant-bot.jar
COPY src/main/java/com/example/restaurant_bot_project/bot/Plan.jpg /root/Plan.jpg
CMD ["java", "-jar", "/restaurant-bot.jar"]