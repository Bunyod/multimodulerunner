#!/bin/bash

screen -dmS "appserver" sh -c "sbt 'project appserver;~reStart'; exec bash" ;  screen -dmS "webserver" sh -c "sbt runWebServer; exec bash"