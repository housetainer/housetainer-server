REPOSITORY=/home/ubuntu/housetainer/production
cd $REPOSITORY

APP_NAME=housetainer

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ] #2
then
  echo "> Can't found process"
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

JAR_NAME=$(ls $REPOSITORY/housetainer-web/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/housetainer-web/build/libs/$JAR_NAME

echo "> Deploy $JAR_PATH" #3
nohup java -jar /home/ubuntu/housetainer/production/housetainer-web/build/libs/$JAR_NAME --spring.config.location=/home/ubuntu/housetainer/application.yaml > /dev/null 2> /dev/null < /dev/null &
