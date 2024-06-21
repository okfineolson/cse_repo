bool x, y, z;
active proctype p(){
    do
        :: true ->
            if
                :: (x || !y || z) && (!x || !y || z) && (x || y || !z) && (!x || y || z) ->
                    printf(" x=%d, y=%d, z=%d\n", x, y, z);
                :: else -> skip;
            fi;
            x = !x;
            if
                :: !x ->
                    y = !y;
                    if
                        :: !y -> 
                            z = !z;
                        :: else -> skip;
                    fi;
                :: else -> skip;
            fi;
    od;
}
init {
    run p();
}

