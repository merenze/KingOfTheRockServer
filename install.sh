#!/bin/bash

JAR_BASENAME=KingOfTheRockServer-DEMO.jar
APP_MANAGER=kingoftherock
SERVICE_NAME=kingoftherock.service
PROJECT_DIR=$(dirname $0)
PRODUCTION_DIR=/opt/$(echo $JAR_BASENAME | cut -f 1 -d '.')

exit_on_fail() {
	if [ $1 -ne 0 ]; then
		echo "Installation failed on ${@:2} (exit code: $1)."
		exit 1
	fi
}

# Require root.
if [ $(whoami) != root ]; then
	echo "Please run this script as root."
	exit 1
fi

# Create the system user to manage the application
useradd -rs /bin/false $APP_MANAGER

# Package the project
mvn -f $PROJECT_DIR/Backend/ clean package

exit_on_fail $?

# Move the JAR to its production location
echo "Deploying the application to $PRODUCTION_DIR/$JAR_BASENAME"

mkdir -p $PRODUCTION_DIR
cp $PROJECT_DIR/Backend/target/$JAR_BASENAME $PRODUCTION_DIR/$JAR_BASENAME

# Give the app manager permissions
chown -R $APP_MANAGER $PRODUCTION_DIR
chgrp -R $APP_MANAGER $PRODUCTION_DIR
chmod -R 775 $PRODUCTION_DIR


# Add the service
echo "Copying service file to /etc/systemd/system/$SERVICE_NAME"
cp $SERVICE_NAME /etc/systemd/system/$SERVICE_NAME
echo "Reloading daemons"
systemctl daemon-reload

echo "Done."
