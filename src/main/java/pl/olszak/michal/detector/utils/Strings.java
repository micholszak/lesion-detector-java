package pl.olszak.michal.detector.utils;

public class Strings {

    public static boolean isNotEmpty(String string){
        return string != null && !string.isEmpty();
    }

    public static boolean areNotEmpty(String... strings){
        for(String string : strings){
            if(!isNotEmpty(string)){
                return false;
            }
        }
        return true;
    }
}
