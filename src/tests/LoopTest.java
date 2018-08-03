package tests;

public class LoopTest {

    /**
     *
     * 循环控制
     * break
     * continue
     */
    public static void main(String[] args){

        loop1:for (int i = 1 ; i < 12 ; i++){
            for (int j = 1 ; j<6 ; j++){
                if(j == 4){
                    break loop1;
                }
                System.out.println("i="+i +",j="+j);
            }
        }

        loop2:for (int i=1 ; i<12 ; i++){
            for (int j=1 ; j<6 ; j++){
                if(i%2==0 && j>4){
                    continue loop2;
                }
                System.out.println("i="+i +",j="+j);
            }
        }

    }

}
