REPOSITORY=/home/ubuntu/housetainer/app/beta
cd $REPOSITORY

APP_NAME=housetainer

CURRENT_PID=$(pgrep -f $APP_NAME/application-beta.yaml)

if [ -z $CURRENT_PID ] #2
then
  echo "> Can't found process"
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

JAR_NAME=$(ls $REPOSITORY/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

echo "> Deploy $JAR_PATH" #3
nohup java -jar $JAR_PATH --spring.config.location=/home/ubuntu/housetainer/application-beta.yaml > /dev/null 2> /dev/null < /dev/null &
