package com.artronics.sdwn.device.buffer;

import java.util.List;

public interface BufferCollector
{
    List<List<Integer>> generateLists(List<Integer> receivedData);
}
