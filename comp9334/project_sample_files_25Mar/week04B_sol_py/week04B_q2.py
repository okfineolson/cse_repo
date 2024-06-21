#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Compare the response time of two queue configurations using simulation
#
Configuration 1: M/M/1 with lamb = 0.9, mu = 1
Configuration 2: M/M/2 with lamb = 0.9, mu = 0.5 for both servers

A simulation program for M/M/m queue has been written, we can use that to
simulation both configurations


"""

import random
import numpy as np 
import matplotlib.pyplot as plt
import sim_mm1_lib as sim_mm1
import sim_mmm_lib as sim_mmm 
import calc_mmm_mrt as calc_mmm


#### 
# If you want to reproduce the results that I give in the tutorial
# solution, you will need to use the setting of the random number 
# generator that I have used. The setting is stored in the file 
# week04B_q2_rand_state.p in the variable rand_state.  
# Comment out the following three lines if you want to use the setting I 
# used. 
# import pickle
# rand_state = pickle.load( open( "week04B_q2_rand_state.p", "rb" ) )
# random.setstate(rand_state)

# 
# I used the following three lines to obtain the state of the random number
# generator and saved it to the file week04B_q2_rand_state.h so that I can
# reproduce the results later. I keep the code here to show you how you can
# save the setting.   
# import pickle
# rand_state = random.getstate()
# pickle.dump( rand_state, open( "week04B_q2_rand_state.p", "wb" ) )

# simulation parameters
lamb = 0.9
mu = 1
time_end = 10000

# Store the mean response time from simulation
n_rep = 10 # Number of replications
avg_response_time_mm1 = np.zeros((n_rep,))
avg_response_time_mmm = np.zeros((n_rep,))

for i in range(n_rep):
    avg_response_time_mm1[i] = sim_mm1.sim_mm1_func(lamb,mu,time_end)
    avg_response_time_mmm[i] = sim_mmm.sim_mmm_func(lamb,mu/2,2,time_end)


# Compute the theoretical value
avg_response_time_theoretical_1 = 1/(mu-lamb)
avg_response_time_theoretical_2 = calc_mmm.mmm(lamb,mu/2,2)

# Plot a graph on the results

plt.plot(np.arange(n_rep),avg_response_time_mm1,'ro',label='M/M/1 simulation')
plt.plot(np.arange(n_rep),avg_response_time_mmm,'bx',label='M/M/2 simulation')
plt.hlines(avg_response_time_theoretical_1,0,9,color='r',linestyles='dashed',label='M/M/1 theoretical')
plt.hlines(avg_response_time_theoretical_2,0,9,color='b',linestyles='dashed',label='M/M/2 theoretical')
plt.legend()
plt.xlabel('Experiment number')
plt.ylabel('Mean response time')
# plt.savefig('fig/week04B_q2_fig.png')
# plt.savefig('fig/week04B_q2_fig.pdf')


