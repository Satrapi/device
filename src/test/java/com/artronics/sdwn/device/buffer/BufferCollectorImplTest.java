package com.artronics.sdwn.device.buffer;

import com.artronics.sdwn.controller.SdwnController;
import com.artronics.sdwn.domain.entities.DeviceConnectionEntity;
import com.artronics.sdwn.domain.entities.packet.PacketEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BufferCollectorImplTest.BeanConfig.class)
public class BufferCollectorImplTest
{
    @Autowired
    BufferCollector convertor;

    List<List<Integer>> act = new ArrayList<>();
    List<Integer> expOnePck = new ArrayList<>();
    List<List<Integer>> expPcks = new ArrayList<>();

    FakeConnectionBuffer fakeBuffers = new FakeConnectionBuffer();
    List<Integer> goodPacket;

    List<Integer> firstOne = fakeBuffers.getFirstOne();
    List<Integer> secondOne = fakeBuffers.getSecondOne();
    List<Integer> twoPackets = fakeBuffers.getTwoPackets();
    List<Integer> withGibberish = fakeBuffers.getWithGibberish();
    List<Integer> withStartStop = fakeBuffers.getWithStartStop();
    List<Integer> withStartStop_anotherPacket = fakeBuffers.getWithStartStop_anotherPacket();
    List<Integer> firstHalf = fakeBuffers.getFirstHalf();
    List<Integer> secondHalf = fakeBuffers.getSecondHalf();
    List<Integer> fullHalf = fakeBuffers.getFullHalf();
    List<Integer> mixed = fakeBuffers.getMixed();

    private static List removeStartStop(List<Integer> packet)
    {
        List exp = packet.subList(1, packet.size() - 1);
        return exp;
    }

    @Before
    public void setUp() throws Exception
    {

    }

    @Test
    public void it_should_return_empty_list_if_there_is_no_packet_ready(){
        act = convertor.generateLists(Arrays.asList(122, 12, 22));

        assertNotNull(act);
        assertThat(act.size(),equalTo(0));
    }

    @Test
    public void it_should_return_a_full_packet_as_it_is()
    {
        act = convertor.generateLists(firstOne);
        expOnePck = removeStartStop(firstOne);

        assertEquals(expOnePck, act.get(0));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void As_long_as_queue_has_data_it_should_create_packets()
    {
        act = convertor.generateLists(twoPackets);
        //first test the size
        assertThat(act.size(), equalTo(2));

        assertEquals(act.get(0), removeStartStop(firstOne));
        assertEquals(act.get(1), removeStartStop(secondOne));
    }

    @Test
    public void It_sould_ignore_un_approprite_bytes_before_start_and_after_stop()
    {
        act = convertor.generateLists(withGibberish);

        assertThat(act.size(), equalTo(2));

        assertEquals(act.get(0), removeStartStop(firstOne));
        assertEquals(act.get(1), removeStartStop(secondOne));
    }

    @Test
    public void If_data_consists_of_start_and_stop_bytes_it_should_not_fuck_up()
    {
        act = convertor.generateLists(withStartStop);

        assertThat(act.size(), equalTo(1));
        assertEquals(act.get(0), removeStartStop(withStartStop));

        //now lets add another packet to end of unibo
        act = convertor.generateLists(withStartStop_anotherPacket);

        assertThat(act.size(), equalTo(2));
        assertEquals(act.get(0), removeStartStop(withStartStop));
        assertEquals(act.get(1), removeStartStop(firstOne));
    }

    @Test
    public void It_should_keep_track_of_previous_bytes()
    {
        //if a buffer consists of half of a message and another
        //received buffer consists of second half. unibo should create
        //a full packet.

        act = convertor.generateLists(firstHalf);
        assertThat(act.size(), equalTo(0));

        act = convertor.generateLists(secondHalf);
        assertThat(act.size(), equalTo(1));

        assertEquals(act.get(0), removeStartStop(fullHalf));
    }

    @Test
    public void Now_everything_together()
    {
        //now we send whole data and check if convertor gets us correct number of packets
        int totalNumberOfPackets = 6;
        act = convertor.generateLists(mixed);

        assertThat(act.size(), equalTo(totalNumberOfPackets));
    }

    @Configuration
    @ComponentScan("com.artronics.sdwn.device.buffer")
    @PropertySource("classpath:application.properties")
    static class BeanConfig{

        @Bean
        public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
        {
            return new PropertySourcesPlaceholderConfigurer();
        }
        @Bean
        public SdwnController getController(){
            return new SdwnController()
            {
                @Override
                public DeviceConnectionEntity registerDeviceConnection(DeviceConnectionEntity device)
                {
                    return null;
                }

                @Override
                public void addPacket(PacketEntity packet)
                {

                }
            };
        }
    }
}