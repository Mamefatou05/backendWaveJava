package com.backendwave.data.seeders;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public abstract class BaseSeeder {
    public abstract void seed() throws IOException, WriterException;
}
