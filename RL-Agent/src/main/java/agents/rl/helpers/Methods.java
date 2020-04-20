package agents.rl.helpers;

import java.util.Random;
import java.util.Set;

public class Methods {
    public static <E> E getRandomElement(Set<? extends E> set){

        Random random = new Random();
        int randomNumber = random.nextInt(set.size());

        int currentIndex = 0;
        E randomElement = null;

        for(E element : set){
            randomElement = element;
            if(currentIndex == randomNumber)
                return randomElement;

            currentIndex++;
        }

        return randomElement;
    }
}
