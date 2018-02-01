package com.gmail.aizperm.sign;

import java.io.IOException;

public interface PhotoSigner
{
    byte[] sign(byte[] data) throws IOException;
}
