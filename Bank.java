public class Bank {
    //number of resource types (input)
    private final int numResourceType;
    // each cell is resource type contain amount of available of it (input)
    private int[] available;
    // number of processes (input)
    private final int numProcess;
    // is the maximum amount of resources that process can use (input)
    private int[][] maximum;
    //the amount currently allocated to each process (input)
    private int[][] allocation;
    //the remaining needs of each process (calculated)
    private int[][] need;

    public Bank ( int nR , int nP , int[][] alloc , int[][] max ) {
        this.numResourceType = nR;
        this.numProcess = nP;

        this.allocation = new int[ numProcess ][ numResourceType ];
        this.allocation = alloc;

        this.maximum = new int[ numProcess ][ numResourceType ];
        this.maximum = max;

        this.need = new int[ numProcess ][ numResourceType ];
        //calculate need
        for ( int i = 0 ; i < numProcess ; i++ ) {
            for ( int j = 0 ; j < numResourceType ; j++ )
                need[ i ][ j ] = maximum[ i ][ j ] - allocation[ i ][ j ];


        }

    }

    public int[] getAvailable ( ) {
        return available;
    }

    public void setAvailable ( int[] available ) {
        this.available = available;
    }

    public boolean Algorithm ( ) {
        boolean[] Finish = new boolean[ numProcess ];
        int[] work = new int[ numResourceType ];
        System.arraycopy ( available , 0 , work , 0 , work.length );
        int[] safeSequence = new int[ numProcess ];
        int currentseq = 0;

        //print state
        System.out.print ( "Processes \t" );
        System.out.print ( "Allocation\t" );
        System.out.print ( "Maximum\t" );
        System.out.println ( "\t need" );

        for ( int i = 0 ; i < numProcess ; i++ ) {
            System.out.print ( "P" + i + "\t\t\t " );

            for ( int j = 0 ; j < numResourceType ; j++ ) {
                System.out.print ( allocation[ i ][ j ] + "  " );
            }
            System.out.print ( " " );
            for ( int j = 0 ; j < numResourceType ; j++ ) {
                System.out.print ( " " + maximum[ i ][ j ] + " " );
            }
            System.out.print ( "\t" );
            for ( int j = 0 ; j < numResourceType ; j++ ) {
                System.out.print ( " " + need[ i ][ j ] + "" );
            }
            System.out.println ( "\n" );

        }
        System.out.print ( "available " );
        for ( int i = 0 ; i < numResourceType ; i++ ) {
            System.out.print ( available[ i ] + " " );
        }
        System.out.println ( "\n---------------------------------------------------------------" );
        //algorithm make until you check all process
        while ( currentseq < numProcess ) {
            boolean found = false;
            //loop on process
            for ( int i = 0 ; i < numProcess ; i++ ) {
                System.out.print ( "Check on Process" + i );
                if ( ! Finish[ i ] ) {
                    System.out.print ( "\t not finished " );
                    int j;
                    for ( j = 0; j < numResourceType ; j++ ) {
                        //we should check all resource type
                        if ( need[ i ][ j ] > work[ j ] ) {
                            System.out.println ( " Resources " + j + ": need ( " + need[ i ][ j ] + " ) > work (" + work[ j ] + ") " );
                            System.out.println ( "must wait" );
                            System.out.println ( "----------------------------------------" );
                            break;
                        }
                    }
                    //all resources type checked
                    if ( j == numResourceType ) {
                        System.out.print ( "need (" );
                        for ( int k = 0 ; k < numResourceType ; k++ ) {
                            System.out.print ( need[ i ][ k ] + " " );
                        }
                        System.out.print ( ") <= work (" );
                        for ( int k = 0 ; k < numResourceType ; k++ ) {
                            System.out.print ( work[ k ] + " " );
                        }
                        System.out.print ( ")" );
                        System.out.println ( "\n" );
                        System.out.print ( "Execute it and then release new work (" );
                        for ( int k = 0 ; k < numResourceType ; k++ ) {
                            work[ k ] = work[ k ] + allocation[ i ][ k ];
                            System.out.print ( work[ k ] + " " );
                        }
                        System.out.print ( ")" );
                        System.out.println ( "\n------------------------------------------" );
                        Finish[ i ] = true;
                        safeSequence[ currentseq++ ] = i;
                        found = true;

                    }
                } else
                    System.out.println ( " Finished" );
            }
            if ( ! found )
                break;
        }
        if ( currentseq < numProcess ) {
            System.out.println ( "unSafe" );
            return false;

        } else {
            System.out.println ( "safe Sequence" );
            for ( int i = 0 ; i < numProcess ; i++ ) {
                System.out.print ( "P" + safeSequence[ i ] );
                if ( i != numProcess - 1 )
                    System.out.print ( " -> " );
            }
            return true;
        }
    }

    void Request ( int processnum , int[] request ) {
        for ( int i = 0 ; i < numResourceType ; i++ ) {
            if ( request[ i ] > need[ processnum ][ i ] ) {
                System.out.println ( "Request is greater than needed Resources" );
                return;
            }

        }
        for ( int i = 0 ; i < numResourceType ; i++ ) {
            if ( request[ i ] > available[ i ] ) {
                System.out.println ( "Request is greater than available Resources" );
                return;
            }
        }
        for ( int i = 0 ; i < numResourceType ; i++ ) {
            allocation[ processnum ][ i ] += request[ i ];
            need[ processnum ][ i ] = maximum[ processnum ][ i ] - allocation[ processnum ][ i ];
            available[ i ] -= request[ i ];
        }
        boolean check = Algorithm ( );
        if ( ! check ) {
            //to release request
            for ( int i = 0 ; i < numResourceType ; i++ ) {
                allocation[ processnum ][ i ] -= request[ i ];
                need[ processnum ][ i ] = maximum[ processnum ][ i ] - allocation[ processnum ][ i ];
                available[ i ] += request[ i ];
            }
        }

    }

    public void Release ( int processnum , int[] release ) {
        boolean check = false;
        for ( int i = 0 ; i < numResourceType ; i++ ) {
            if ( release[ i ] > allocation[ processnum ][ i ] ) {
                System.out.println ( "release resources are greater than allocated resources " );
                //check=true;
                return;
            }

        }
        for ( int i = 0 ; i < numResourceType ; i++ ){
            allocation[ processnum ][ i ] = allocation[ processnum ][ i ] - release[ i ];
            available [i]= available[i]+release[i];
        }
        //this for to check allocation of resources---remove it
        for ( int i = 0 ; i < numResourceType ; i++ )
            System.out.print ( allocation[ processnum ][ i ] + " " );
    }
}