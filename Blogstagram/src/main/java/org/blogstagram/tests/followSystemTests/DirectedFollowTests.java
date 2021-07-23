package org.blogstagram.tests.followSystemTests;


import org.blogstagram.models.DirectedFollow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;




@RunWith(MockitoJUnitRunner.class)
public class DirectedFollowTests {
    @Spy
    private DirectedFollow directedFollow;
    @Captor
    private ArgumentCaptor<Date> dateCaptor;

    @Test
    public void testDirectedFollow1(){
        assertNotNull(directedFollow);
        assertEquals(DirectedFollow.NO_ID, directedFollow.getId());
        directedFollow.setId(5);
        Mockito.verify(directedFollow, times(1)).setId(Mockito.anyInt());
        assertEquals( 5, directedFollow.getId());
        Mockito.verify(directedFollow, times(2)).getId();
    }

    @Test
    public void testDirectedFollow2(){
        assertNotNull(directedFollow);
        assertEquals(DirectedFollow.NO_ID, directedFollow.getId());
        Date currentDate = new Date();
        directedFollow.setCreatedAt(currentDate);
        verify(directedFollow).setCreatedAt(dateCaptor.capture());
        assertEquals(currentDate, dateCaptor.getValue());
        verify(directedFollow, times(1)).setCreatedAt(currentDate);
        assertEquals(currentDate, directedFollow.getCreatedAt());
        Date newDate = new Date();
        directedFollow.setCreatedAt(newDate);
        verify(directedFollow, times(2)).setCreatedAt(Mockito.any());
        assertEquals(newDate, directedFollow.getCreatedAt());
    }

    @Test
    public void testDirectedFollow3(){
        assertNotNull(directedFollow);
        Integer fromId = 1;
        directedFollow.setFromId(fromId);
        verify(directedFollow, times(1)).setFromId(Mockito.anyInt());
        assertEquals(fromId, (Integer) directedFollow.getFromId());
        verify(directedFollow, times(1)).getFromId();
        directedFollow.setFromId(3);
        verify(directedFollow, times(2)).setFromId(Mockito.anyInt());
        assertEquals(3, directedFollow.getFromId());
        verify(directedFollow, times(2)).getFromId();
    }

    @Test
    public void testDirectedFollow4(){
        assertNotNull(directedFollow);
        Integer toId = 3;
        directedFollow.setToId(toId);
        verify(directedFollow, times(1)).setToId(Mockito.anyInt());
        assertEquals(toId, (Integer) directedFollow.getToId());
        verify(directedFollow, times(1)).getToId();
        directedFollow.setToId(3);
        verify(directedFollow, times(2)).setToId(Mockito.anyInt());
        assertEquals(3, directedFollow.getToId());
        verify(directedFollow, times(2)).getToId();
    }

}
