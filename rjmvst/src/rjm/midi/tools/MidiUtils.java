package rjm.midi.tools;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import jvst.wrapper.valueobjects.VSTEvent;
import jvst.wrapper.valueobjects.VSTMidiEvent;
import rjm.vst.tools.VstUtils;

public class MidiUtils {

    //This function assumes inputValue will be 0-127
    public static int getScaledMidiValue(int inputValue, int maxOutputValue, int minOutputValue)
    {
	double ratio = inputValue / 127;
	int delta = maxOutputValue - minOutputValue;
	int newval = (int)(delta * ratio + minOutputValue);
	return newval;
    }

    
    public static Note getNote(ShortMessage message) throws Exception
    {
	byte[] msg_data = (message.getMessage());

	int note, vel;
	note = getData1FromMidiByteArray(msg_data);
	vel = getData2FromMidiByteArray(msg_data);

	return new Note(note, vel);
    }
    
    public static ShortMessage getShortMessage(VSTMidiEvent event)
    {
	if( event.getType() == VSTEvent.VST_EVENT_MIDI_TYPE )
	{
	    try
	    {
		VstUtils.out("test this getShortMessage()");
		byte[] msg_data = ((VSTMidiEvent)event).getData();

		int msg_channel = MidiUtils.getChannelFromMidiByteArray(msg_data);
		int ctrl_index = MidiUtils.getData1FromMidiByteArray(msg_data);
		int ctrl_value = MidiUtils.getData2FromMidiByteArray(msg_data);
		int status = MidiUtils.getStatusWithoutChannelByteFromMidiByteArray(msg_data);

		ShortMessage s;
		s = new ShortMessage(status,  msg_channel - 1, ctrl_index, ctrl_value);
		return s;
	    } 
	    catch (InvalidMidiDataException e)
	    { }
	}
	return null;
    }

    
    
    public static int getChannelFromMidiByteArray(byte[] msg_data)
    { return ( msg_data[ 0 ] & 0xF ) + 1; }
    
    public static int getData1FromMidiByteArray(byte[] msg_data)
    { return msg_data[ 1 ] & 0x7F; }

    public static int getData2FromMidiByteArray(byte[] msg_data)
    { return msg_data[ 2 ] & 0x7F; }
    
    public static int getStatusFromMidiByteArray(byte[] msg_data)
    { return (msg_data[ 0 ] & 0xF0) >> 4; }
    
    public static int getStatusWithoutChannelByteFromMidiByteArray(byte[] msg_data)
    {
	int msg_channel = ( msg_data[ 0 ] & 0xF ) + 1;
	int status = (int) (msg_data[0] & 0xFF) - msg_channel + 1; //different but related to msg_status
	return status;
    }
}
