public class CigaretteSmokers {
    static Semaphore3151 agent = new JavaSemaphore(1);
    static Semaphore3151 tobacco = new JavaSemaphore(0);
    static Semaphore3151 paper = new JavaSemaphore(0);
    static Semaphore3151 match = new JavaSemaphore(0);

    static Semaphore3151 semaphoreA = new JavaSemaphore(0);
    static Semaphore3151 semaphoreB = new JavaSemaphore(0);
    static Semaphore3151 semaphoreC = new JavaSemaphore(0);
    static Semaphore3151 pusher = new JavaSemaphore(1);
    

    /* TODO: Add more semaphores and shared variables here as needed */
    static Boolean MatchState = false;
    static Boolean TobaccoState = false;
    static Boolean PaperState = false;
    public static void main(String[] args) {
        AgentA a = new AgentA();
        AgentB b = new AgentB();
        AgentC c = new AgentC();
        SmokerA sa = new SmokerA();
        SmokerB sb = new SmokerB();
        SmokerC sc = new SmokerC();
        PusherM pm = new PusherM();
        PusherT pt = new PusherT();
        PusherP pp = new PusherP();
        /* TODO: Add and start more threads here as needed */
        a.start();
        b.start();
        c.start();
        sa.start();
        sb.start();
        sc.start();
        pm.start();
        pt.start();
        pp.start();
    }

    /* TODO: Add more classes here for threads as needed */

    // Smoker with Tobacco
    public static class SmokerA extends Thread {

        private void smoke() {
            System.out.println("SMOKEA: Got a paper and matches. Puff Puff.");
        }

        @Override
        public void run() {
            while (true) {
                /* FIXME: This code deadlocks! */
                semaphoreA.P();
                smoke();
                agent.V();

            }
        }
    }

    // Smoker with Paper
    public static class SmokerB extends Thread {

        private void smoke() {
            System.out.println("SMOKEB: Got tobacco and matches. Puff Puff.");
        }

        @Override
        public void run() {
            while (true) {
                /* FIXME: This code deadlocks! */
                semaphoreB.P();
                smoke();
                agent.V();
            }
        }
    }

    // Smoker with Matches
    public static class SmokerC extends Thread {

        private void smoke() {
            System.out.println("SMOKEC: Got tobacco and paper. Puff Puff.");
        }

        @Override
        public void run() {
            while (true) {
                /* FIXME: This code deadlocks! */
                semaphoreC.P();
                smoke();
                agent.V();
            }
        }
    }

    public static class PusherM extends Thread {
        @Override
        public void run() {
            while (true) {
                match.P();
                pusher.P();
                MatchState = true;
                if (PaperState)
                {
                    PaperState = false;
                    MatchState = false;
                    semaphoreA.V();
                    pusher.V();
                    continue;
                }
                if (TobaccoState)
                {
                    PaperState = false;
                    MatchState = false;
                    semaphoreB.V();
                    pusher.V();
                    continue;
                }
                pusher.V();
            }
        }
    }
    public static class PusherT extends Thread {
        @Override
        public void run() {
            while (true) {
                tobacco.P();
                pusher.P();
                TobaccoState = true;
                if (MatchState)
                {
                    //Consume
                    MatchState = false;
                    TobaccoState = false;
                    semaphoreB.V();
                    pusher.V();
                    continue;
                }
                if (PaperState)
                {
                    //Consume
                    PaperState = false;
                    TobaccoState = false;
                    semaphoreC.V();
                    pusher.V();
                    continue;
                }
                pusher.V();
            }
        }
    }
    public static class PusherP extends Thread {
        @Override
        public void run() {
            while (true) {
                paper.P();
                pusher.P();
                PaperState = true;
                if (TobaccoState)
                {
                    //Consume
                    PaperState = false;
                    TobaccoState = false;
                    semaphoreC.V();
                    pusher.V();
                    continue;
                }
                if (MatchState)
                {
                    //Consume
                    PaperState = false;
                    MatchState = false;
                    semaphoreA.V();
                    pusher.V();
                    continue;
                }
                pusher.V();
            }
        }
    }
    /* Do not change anything below this line */
    public static class AgentA extends Thread {
        @Override
        public void run() {
            while (true) {
                agent.P();
                System.out.println("AGENTA: Supplying tobacco and paper");
                tobacco.V();
                paper.V();
            }
        }
    }

    public static class AgentB extends Thread {
        @Override
        public void run() {
            while (true) {
                agent.P();
                System.out.println("AGENTB: Supplying paper and match");
                paper.V();
                match.V();
            }
        }
    }

    public static class AgentC extends Thread {
        @Override
        public void run() {
            while (true) {
                agent.P();
                System.out.println("AGENTC: Supplying tobacco and match");
                tobacco.V();
                match.V();
            }
        }
    }

}
