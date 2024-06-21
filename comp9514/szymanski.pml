#include "critical2.h"

byte flag[3] = {0,0,0};

inline hold_door() {
  flag[_pid] = 2;  
  do
  :: b = 0;
     b = b || (flag[0] == 4);
     b = b || (flag[1] == 4);
     b = b || (flag[2] == 4);
     if
     :: b -> break;
     :: else -> skip;
     fi;
  od;
}

active[3] proctype P() {
  bit b = 0;
  do
  :: non_critical_section();
     flag[_pid] = 1;
     /* Await FORALLj. flag[j] < 3 */
     flag[0] < 3;
     flag[1] < 3;
     flag[2] < 3;
     flag[_pid] = 3;
     /* if EXISTS j. flag[2] = 1 */     
     b = 0;
     b = b || (flag[0] == 1);
     b = b || (flag[1] == 1);
     b = b || (flag[2] == 1);
     if
     :: b -> hold_door();
     :: else -> skip;
     fi;
     
    // Reorder the await statements
    _pid > 0 || flag[1] < 2 || flag[1] > 3;     
    _pid > 1 || flag[2] < 2 || flag[2] > 3;

    flag[_pid] = 4;     
    _pid == 0 || flag[0] < 2;
    _pid <= 1 || flag[1] < 2;

    critical_section();

    // Reorder the await statements
    flag[_pid] = 0;
    
    od
}
