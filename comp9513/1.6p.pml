bool x, y, z;
init {
    bool found = false;
    int i;
    select(i: 0 .. 7) 
        x = (i & 1) != 0;
        y = (i & 2) != 0;
        z = (i & 4) != 0;
        if
        :: (x || !y || z) && (!x || !y || z) && (x || y || !z) && (!x || y || z) -> 
            printf("Satisfiable: x=%d, y=%d, z=%d\n", x, y, z);
            found = true;
        :: else -> skip;
        fi;
    
	if
	::!found->printf("nothing Satisfiable");
	fi
}

