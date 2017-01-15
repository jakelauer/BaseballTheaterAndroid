package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public enum League implements Serializable {
	AA,
	NN
}
