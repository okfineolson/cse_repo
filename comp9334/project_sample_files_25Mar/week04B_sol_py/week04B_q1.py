#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
COMP9334 Capacity Planning

Revision problem: Week 4B, Question 1

Run the M/M/1 simulation with length

"""

import random
import numpy as np 
import matplotlib.pyplot as plt
import sim_mm1_lib as sim_mm1 


#### 
# If you want to reproduce the results that I give in the tutorial
# solution, you will need to use the setting of the random number 
# generator that I have used. The setting is stored in the file 
# week04B_q1_rand_state.p in the variable rand_state.  
# Comment out the following three lines if you want to use the setting I 
# used. 
# import pickle
# rand_state = pickle.load( open( "week04B_q1_rand_state.p", "rb" ) )
# random.setstate(rand_state)

# 
# I used the following three lines to obtain the state of the random number
# generator and saved it to the file week04B_q1_rand_state.h so that I can
# reproduce the results later. I keep the code here to show you how you can
# save the setting.   
# import pickle
# rand_state = random.getstate()
# pickle.dump( rand_state, open( "week04B_q1_rand_state.p", "wb" ) )


# Define the simulation parameters 
lamb = 0.7
mu = 1


# An array of simulation end times
time_end_array = np.array([1000, 5000, 10000, 50000])

# Array response_time_array stores the mean response times from
# different simulation end times and replications 
response_time_array = np.zeros((4,20))
# 
# response_time_array[i,j]
#   i runs from 1 to 4, for different end times
#   j runs from 1 to 20, for 20 different simulations 

# iteration 
for i in range(4):
    for j in range(20):
        response_time_array[i,j] = sim_mm1.sim_mm1_func(lamb,mu,time_end_array[i])

  
# expected results from M/M/1 theory
response_time_mm1_theory = 1/(mu-lamb)

# For a given arrival rate, increase the simulation length
plt.semilogx(time_end_array,response_time_array,'bo')
plt.hlines(response_time_mm1_theory,min(time_end_array),max(time_end_array),color='r')
plt.xlabel('Length of simulation')
plt.ylabel('Response time')
plt.title('Horizontal line - theoretical value. circles - simulated values')
# plt.savefig('fig/week04B_q1_fig.pdf')

# Mean over 20 replications for each value of T
mean_mrt = np.mean(response_time_array,axis=1)

# standard deviation
std_mrt = np.std(response_time_array,axis=1)


