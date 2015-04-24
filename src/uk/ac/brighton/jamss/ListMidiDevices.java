package uk.ac.brighton.jamss;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;


public class ListMidiDevices {

	public static void main(String[] args) {
		try {
			MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
			for ( MidiDevice.Info midiDeviceInfo : midiDeviceInfoArray ) {
				System.out.println("Next device:");
				System.out.println("  Name: " + midiDeviceInfo.getName());
				System.out.println("  Description: " + midiDeviceInfo.getDescription());
				System.out.println("  Vendor: " + midiDeviceInfo.getVendor());
				System.out.println("  toString(): " + midiDeviceInfo.toString());
				System.out.println("");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

