#!/bin/bash

nohup java -jar ./gateway/target/gateway-1.0.0-SNAPSHOT.jar >> ./logs/gateway.out &
nohup java -jar ./user/target/user-1.0.0-SNAPSHOT-exec.jar >> ./logs/user.out &
nohup java -jar ./goods/target/goods-1.0.0-SNAPSHOT-exec.jar >> ./logs/goods.out &