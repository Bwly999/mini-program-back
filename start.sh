#!/bin/bash

nohup java -jar ./gateway/target/gateway-1.0.0-SNAPSHOT.jar >> ./logs/gateway.out &
nohup java -jar ./user/target/user-1.0.0-SNAPSHOT.jar >> ./logs/user.out &
nohup java -jar ./godds/target/godds-1.0.0-SNAPSHOT.jar >> ./logs/godds.out &