package ru.netology.graphics.image;

public class CharColorConverter implements TextColorSchema {
    char[] ch = {'#', '&', '@', '%', '*', '+', '-', ' '};

    @Override
    public char convert(int color) {
        //  char[] ch = {'#', '&', '@', '%', '*', '+', '-', ' '}; // нужно выносить
        //  или создается каждый раз при вызове метода и это дорого
        return ch[color / 32];
    }
}
