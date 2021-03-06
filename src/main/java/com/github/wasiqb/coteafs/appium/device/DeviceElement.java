/**
 * Copyright (c) 2017, Wasiq Bhamla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wasiqb.coteafs.appium.device;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.github.wasiqb.coteafs.appium.config.enums.AutomationType;
import com.github.wasiqb.coteafs.appium.config.enums.PlatformType;
import com.github.wasiqb.coteafs.appium.config.enums.WaitStrategy;

/**
 * @author wasiq.bhamla
 * @since 25-Apr-2017 7:29:23 PM
 */
public class DeviceElement {
	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:34:42 PM
	 * @param name
	 * @return instance
	 */
	public static DeviceElement create (final String name) {
		return new DeviceElement (name);
	}

	private final List <DeviceElement>							childs;
	private int													index;
	private final Map <PlatformType, Map <AutomationType, By>>	locators;
	private final String										name;
	private DeviceElement										parent;
	private WaitStrategy										wait;

	private DeviceElement (final String name) {
		this.childs = new ArrayList <> ();
		this.name = name;
		this.wait = WaitStrategy.NONE;
		this.locators = new EnumMap <> (PlatformType.class);
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:49:17 PM
	 * @return childs
	 */
	public List <DeviceElement> childs () {
		return this.childs;
	}

	/**
	 * @author wasiqb
	 * @since Nov 4, 2018
	 * @param automation
	 * @param findBy
	 * @return instance
	 */
	public DeviceElement forAndroid (final AutomationType automation, final By findBy) {
		return using (PlatformType.ANDROID, automation, findBy);
	}

	/**
	 * @author wasiqb
	 * @since Nov 4, 2018
	 * @param findBy
	 * @return instance
	 */
	public DeviceElement forAndroid (final By findBy) {
		return using (PlatformType.ANDROID, AutomationType.APPIUM, findBy);
	}

	/**
	 * @author wasiqb
	 * @since Nov 4, 2018
	 * @param automation
	 * @param findBy
	 * @return instance
	 */
	public DeviceElement forIos (final AutomationType automation, final By findBy) {
		return using (PlatformType.IOS, automation, findBy);
	}

	/**
	 * @author wasiqb
	 * @since Nov 4, 2018
	 * @param findBy
	 * @return instance
	 */
	public DeviceElement forIos (final By findBy) {
		return using (PlatformType.IOS, AutomationType.XCUI, findBy);
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:45:09 PM
	 * @return index
	 */
	public int index () {
		return this.index;
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:44:39 PM
	 * @param location
	 * @return instance
	 */
	public DeviceElement index (final int location) {
		this.index = location;
		return this;
	}

	/**
	 * @author wasiqb
	 * @since Oct 23, 2018
	 * @return by locator
	 */
	public By locator () {
		return locator (AutomationType.APPIUM);
	}

	/**
	 * @author wasiqb
	 * @since Oct 23, 2018
	 * @param automation
	 * @return by locator
	 */
	public By locator (final AutomationType automation) {
		return locator (PlatformType.ANDROID, automation);
	}

	/**
	 * @author wasiqb
	 * @since Oct 23, 2018
	 * @param platform
	 * @param automation
	 * @return by locator
	 */
	public By locator (final PlatformType platform, final AutomationType automation) {
		final Map <AutomationType, By> locator = this.locators.get (platform);
		if (!locator.containsKey (automation) && automation != AutomationType.APPIUM)
			return locator.get (AutomationType.APPIUM);
		return locator.get (automation);
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:35:06 PM
	 * @return name
	 */
	public String name () {
		return this.name;
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:50:29 PM
	 * @return parent
	 */
	public DeviceElement parent () {
		return this.parent;
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:49:46 PM
	 * @param elementParent
	 * @return instance
	 */
	public DeviceElement parent (final DeviceElement elementParent) {
		if (this.parent == null) {
			this.parent = elementParent;
		}
		if (!elementParent.childs ()
			.contains (this)) {
			elementParent.addChild (this);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString () {
		final String line1 = "Name: %s";
		final String line2 = "By: %s";
		final String line4 = "Index: %d";
		final String line3 = "Childs: %s";
		final StringBuilder sb = new StringBuilder (format (line1, this.name)).append ("\n");
		sb.append (format (line2, this.locators))
			.append ("\n");
		sb.append (format (line4, this.index))
			.append ("\n");
		sb.append (format (line3, this.childs))
			.append ("\n");
		return sb.toString ();
	}

	/**
	 * @author wasiq.bhamla
	 * @since Jan 30, 2018 7:32:07 PM
	 * @return {@link WaitStrategy}
	 */
	public WaitStrategy waitStrategy () {
		return this.wait;
	}

	/**
	 * @author wasiq.bhamla
	 * @since Jan 30, 2018 7:32:23 PM
	 * @param strategy
	 * @return instance
	 */
	public DeviceElement waitStrategy (final WaitStrategy strategy) {
		this.wait = strategy;
		return this;
	}

	/**
	 * @author wasiq.bhamla
	 * @since 25-Apr-2017 7:48:31 PM
	 * @param androidElement
	 */
	private void addChild (final DeviceElement child) {
		this.childs.add (child);
		if (child.parent () == null || !child.parent ()
			.equals (this)) {
			child.parent (this);
		}
	}

	private DeviceElement using (final PlatformType platform, final AutomationType automation,
		final By findBy) {
		Map <AutomationType, By> platformLocator = new EnumMap <> (AutomationType.class);
		if (this.locators.containsKey (platform)) {
			platformLocator = this.locators.get (platform);
		}
		platformLocator.put (automation, findBy);
		this.locators.put (platform, platformLocator);
		return this;
	}
}