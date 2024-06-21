#!/usr/bin/env python3
# -*- coding: utf-8 -*-


import sys 
import os
import time

import random
import numpy as np 
from jobclass import *

"""
num_of_server = 6
lamda = 3.1
alpha2l=0.91
alpha2u=1.27
servers={"p1":0.32,"p2":0.21,"p3":0.15,"p4":0.08,"p5":0.24}
"""    
seed = 1
random.seed(seed)


    
def get_server_time(alpha, beta):
    r = (beta - 1) / (alpha ** (1 - beta))
    x = random.uniform(alpha, 2)
    y = random.uniform(0, r / (alpha ** beta) + 1)
    while y > r / (x ** beta):
        x = random.uniform(alpha, 2)
        y = random.uniform(0, r / (alpha ** beta) + 1)
    return x
def main(s):
    config_folder = 'config'
    interarrival_file = os.path.join(config_folder,'interarrival_'+s+'.txt')
    para_file = os.path.join(config_folder,'para_'+s+'.txt')
    service_file = os.path.join(config_folder,'service_'+s+'.txt')
    mode_file = os.path.join(config_folder, f"mode_{s}.txt")
    
    with open(mode_file) as file:
        mode = file.read()
    
    if mode == "random":
        beta,alpha = np.loadtxt(service_file)
        n,h,et = np.loadtxt(para_file).astype(int)
        interarrival_p = np.loadtxt(interarrival_file, skiprows=1)
        with open(interarrival_file,'r') as file:
            readinterarrival = file.readline() 
        lamda, alpha2l, alpha2u = [float(i) for i in (readinterarrival.split(' ')[:-1])]
        arrival = []
        cumulative_T = 0.0
        while cumulative_T < et:
            new_arrival = random.expovariate(lamda) * random.uniform(alpha2l,alpha2u)
            arrival.append(new_arrival)
            cumulative_T += new_arrival
        cumulative_P = np.cumsum(interarrival_p)
        server_T = np.random.uniform(0,1,len(arrival))
        
        for i in range(0,interarrival_p.size-1):
            #num = (cumulative_P[i] < server_T) & (server_T <= cumulative_P[i + 1])
            #print(num)
            server_T[(cumulative_P[i] < server_T) & (server_T <= cumulative_P[i + 1])] = i + 2.
        server_T[(server_T <= cumulative_P[0])] = 1.0
        services = np.zeros((len(arrival), int(server_T.max())))
        services[services == 0] = np.nan
        for x, visit in enumerate(server_T):
            for y in range(int(visit)):
                services[x][y] = get_server_time(alpha, beta)
    else:#if mode == trace
        arrival = np.loadtxt(interarrival_file)
        services = np.loadtxt(service_file)
        n, h = np.loadtxt(para_file).astype(int)

    allJob = []
    nextArrival = 0.0
    for i, interval in enumerate(arrival):
        nextArrival += interval
        totalVisitTime = (~np.isnan(services[i])).sum()
        serviceTime = services[i]
        allJob.append(Job(index=i+1, arrivalTime=nextArrival, serviceTime=serviceTime, totalVisitTime=totalVisitTime))

    dispatcher = Dispatcher(h, n)

    loadvalue(allJob,s,dispatcher)
def loadvalue(Job_list, s,dispatcher):
    if os.path.exists(os.path.join("output", f"dep_{s}.txt")):
        os.remove(os.path.join("output", f"dep_{s}.txt"))
    if os.path.exists(os.path.join("output", f"mrt_{s}.txt")):
        os.remove(os.path.join("output", f"mrt_{s}.txt"))
    dispatcher.simulate(Job_list, s)
    response = []
    for i in Job_list:
        response.append(i.responseTime)
    mrt = np.array(response).mean()
    with open(os.path.join("output", f"mrt_{s}.txt"),'a') as f:
        f.write(f"{mrt:.4f}")
    return mrt
if __name__ == "__main__":
   main(sys.argv[1])