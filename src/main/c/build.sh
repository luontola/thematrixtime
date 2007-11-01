#!/bin/sh
g++ -B . -Wall -shared -o libsystemclock.so systemclock.c
