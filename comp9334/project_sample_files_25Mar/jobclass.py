import numpy as np
import os

class Job:
    def __init__(self, index, arrivalTime, serviceTime, totalVisitTime):
        """
        index: index of job
        arrivalTime: jobs arrival Time
        serviceTime: jobs in service Time
        totalVisitTime: jobs total visit time
        """
        self.index = index
        self.arrivalTime = arrivalTime
        self.serviceTime = serviceTime
        self.departTime = np.inf
        self.responseTime = 0.0
        self.totalVisitTime = totalVisitTime
        self.currentTime = 0
    def current(self):
        return self.currentTime
    def checkinit(self,time):
        if self.currentTime == self.totalVisitTime:
            self.departTime = time
            self.responseTime = time - self.arrivalTime
            return True
        else:
            return False
            

class Server:
    def __init__(self):
        """
        master_clock:time in server
        """
        self.master_clock = None
        self.newserver()

    def newserver(self):
        self.idle = True
        self.job = None
        self.master_clock = 0.0
        self.leaveTime = np.inf

    def state(self, time, s):

        self.master_clock = time
        checkserver = np.abs(self.master_clock - self.leaveTime)#check if the job already leave the server
        if checkserver < 1e-8:
            with open(os.path.join("output", f"dep_{s}.txt"), 'a') as f:
                jobmessage = f"{self.job.arrivalTime:.4f} {time:.4f} {self.job.currentTime + 1} {self.job.totalVisitTime}"
                f.write("%s\n"%(jobmessage))
            self.job.currentTime += 1
            if self.job.checkinit(time):
                self.newserver()
                return None, True
            job = self.job
            self.newserver()
            return job, True
        else:
            return None, self.idle

    def newjob(self, job, t):
        self.master_clock = t
        self.job = job
        self.idle = False
        self.leaveTime = self.master_clock + job.serviceTime[job.currentTime]


class Dispatcher:
    def __init__(self, h, n):
        """
        h: threshold of the dispatcher
        n: nums of the server farm
        output: output folder
        """
        self.threshold = h
        self.cores = n
        self.queue_low = []
        self.queue_high = []
        self.master_clock = 0.0
        self.servers = [Server() for _ in range(n)]


    def checkidle(self):
        for server in self.servers:
            if(not server.idle):
                return False
        return True

    def newjob(self, job, time):

        for server in self.servers:
            if server.idle:
                server.newjob(job, time)
                return True
            
        return False
    def __len__(self):
        return len(self.queue_low) + len(self.queue_high)
    def inqueue(self, job):
        if job.current() < self.threshold:
            self.queue_high.append(job)
        else:
            self.queue_low.append(job)
    def outqueue(self):
        if len(self):
            if len(self.queue_high):
                return self.queue_high.pop(0)
            return self.queue_low.pop(0)
        return None
    def state(self, s):
        for server in self.servers:
            job, boolean = server.state(time=self.master_clock, s=s)
            server.idle = boolean
            if job is not None:
                return job
    def latest_event(self,minimum):
        for server in self.servers:
            minimum = min(server.leaveTime, minimum)
        return minimum
    def simulate(self, Job_list, s):

        count = 0
        while True:
            if not (len(Job_list) or len(self) or ~self.isAllIdle()):
                break
            latest_event = np.inf
            job = None
            if count < len(Job_list):
                job = Job_list[count]
                arrival = job.arrivalTime
            else:
                arrival = np.inf
            latest_event = self.latest_event(latest_event)#####here

            if latest_event >= arrival:
                if job is None:
                    break
                count = count + 1
                self.master_clock = arrival
                self.state(s)
                if not self.newjob(job, self.master_clock):
                    self.inqueue(job)
            else:
                
                self.master_clock = latest_event
                job = self.state(s)
                if job is not None:
                    self.inqueue(job)
                job = self.outqueue()
                if job is None:
                    continue
                if not self.newjob(job, self.master_clock):
                    self.inqueue(job)


