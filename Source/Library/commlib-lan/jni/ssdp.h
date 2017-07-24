#ifndef SSDP_HEADER
#define SSDP_HEADER

#include "parser.h"

//Opens SSDP socket
//Returns -1 on error

int openSocket();

//Closes SSDP socket
void closeSocket();

//Starts discovery thread
//Returns -1 if error
//registerCallback should be started prior running startDiscovery()
int startDiscovery();
void stopDiscovery();

//Sends SSDP search broadcast. MX harcoded to 3
int sendBroadcastMX3();

int sendBroadcastMX5();

//Register callback function that takes pointer to SsdpStructure as argument
void registerCallback(void(*)(SsdpStruct *));

#endif
