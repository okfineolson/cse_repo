#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
COMP9334 

Calculate mean response time of an M/M/m queue 
"""

def mmm(lamb,mu,m):

    # This function calculates the response time T of an M/M/m 
    # queue with 
    # lamb = mean arrival rate  
    # mu = mean service rate 
    # m = number of servers
    #
    # Chun Tung Chou, UNSW   
    # 
    
    # utilisation
    rho = lamb/mu/m
    
    # form an array with (m rho)^k / k! for k = 1,...,m
    x = []
    x.append(m*rho)
    for k in range(2,m+1):
        x.append(x[-1]*m*rho/k)

    
    # The waiting time expression
    C_num = x[-1]
    C_den = (1-rho)*(1+sum(x[:-1])) + x[-1]
    C = C_num/C_den
    
    # Mean response time = Mean service time + Mean waiting time 
    mean_response_time = (1/mu)*(1+ C/m/(1-rho)) 
    
    return mean_response_time
    
