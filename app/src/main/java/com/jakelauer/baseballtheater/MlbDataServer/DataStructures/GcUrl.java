package com.jakelauer.baseballtheater.MlbDataServer.DataStructures;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.jsoup.helper.StringUtil;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class GcUrl
{
	@Attribute(name = "cid")
	public String cidString;

	@Nullable
	public Long getCid()
	{
		Long cid = null;

		if (!TextUtils.isEmpty(cidString))
		{
			cid = Long.parseLong(cidString);
		}

		return cid;
	}
}
