#!/bin/bash

# ls /dev/console
# ls /dev/

# log=$(ls /dev/)
# echo "$log" > /dev/console

log=$(ls /dev/)
echo -n "$log" > /dev/kmsg

# tree /dev/
# log=$(tree /dev/)
# echo "$log" > /dev/console

while true;
do
    # echo "test kmsg" > /dev/kmsg
    echo "test console" > /dev/console
    sleep 5
done
