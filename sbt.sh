java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar bin/sbt-launch.jar "$@"

