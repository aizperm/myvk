package com.gmail.aizperm.sign;

import java.io.IOException;

public interface PhotoSigner
{
    byte[] sign(String locale, Integer type, byte[] data) throws IOException;
    
    byte[] sign(String text, String fontName, byte[] data) throws IOException;
}
