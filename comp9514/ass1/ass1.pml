#define N 10
int numbers[N];
int size = 0;
int readers = 0;
chan readlock = [1] of {bit};
chan writelock = [1] of {bit};

inline insert(x) {
    writelock ? 1;
    // insert operation
    writelock ! 1;
}

inline delete(x) {
    writelock ? 1;
    // delete operation
    writelock ! 1;
}

inline cleanup() {
    writelock ? 1;
    //cleanup operation
    writelock ! 1;
}

inline member(x) {
    readlock ? 1;
    readers++;
    if (readers == 1) {
        writelock ? 1;
    }
    readlock ! 1;

    //member operation

    readlock ? 1;
    readers--;
    if (readers == 0) {
        writelock ! 1;
    }
    readlock ! 1;
}

inline print_sorted() {
    readlock ? 1;
    readers++;
    if (readers == 1) {
        writelock ? 1;
    }
    readlock ! 1;

    // print_sorted operation

    readlock ? 1;
    readers--;
    if (readers == 0) {
        writelock ! 1;
    }
    readlock ! 1;
}

active proctype writer() {
    int x;
    do
        :: true ->
            if
                :: true -> insert(x)
                :: true -> delete(x)
                :: true -> cleanup()
            fi;
    od
}

active proctype reader() {
    int x;
    do
        :: true ->
            if
                :: true -> member(x)
                :: true -> print_sorted()
            fi;
    od
}

