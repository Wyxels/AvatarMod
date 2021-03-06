/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/

package com.crowsofwar.gorecore.config;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ConfigurationException extends RuntimeException {
	
	private ConfigurationException(String message) {
		super(message);
	}
	
	private ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * The end-user made a mistake by creating an invalid configuration file.
	 * 
	 * @author CrowsOfWar
	 */
	public static class UserMistake extends ConfigurationException {
		
		public UserMistake(String message) {
			super(message);
		}
		
	}
	
	/**
	 * An exception occurred while trying to access the configuration file on
	 * disk.
	 * 
	 * @author CrowsOfWar
	 */
	public static class LoadingException extends ConfigurationException {
		
		public LoadingException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public LoadingException(String message) {
			super(message);
		}
		
	}
	
	/**
	 * An exception occurred while using reflection to set values of the object.
	 * 
	 * @author CrowsOfWar
	 */
	public static class ReflectionException extends ConfigurationException {
		
		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}
		
	}
	
	/**
	 * An exception occurred for some other reason, which we haven't explained
	 * 
	 * @author CrowsOfWar
	 */
	public static class Unexpected extends ConfigurationException {
		
		public Unexpected(String message, Throwable cause) {
			super(message, cause);
		}
		
	}
	
}
