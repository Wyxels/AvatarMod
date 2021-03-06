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

package com.crowsofwar.gorecore.tree;

import net.minecraft.command.ICommandSender;

import com.crowsofwar.gorecore.tree.TreeCommandException.Reason;

import java.util.*;

/**
 * An argument which allows the user to specify one of several values. Supports
 * tab completion.
 *
 * @param <T> The type of value
 * @author CrowsOfWar
 */
public class ArgumentOptions<T> implements IArgument<T> {

	private final List<T> options;
	private final ITypeConverter<T> convert;
	private final String name;
	private T defaultValue;

	@SafeVarargs
	public ArgumentOptions(ITypeConverter<T> convert, String name, T... options) {
		this.options = Arrays.asList(options);
		defaultValue = null;
		this.convert = convert;
		this.name = name;
	}

	@Override
	public boolean isOptional() {
		return defaultValue != null;
	}

	public ArgumentOptions setOptional(T defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public T convert(String input) {
		T converted = convert.convert(input);
		if (!options.contains(converted)) {
			throw new TreeCommandException(Reason.NOT_OPTION, input, getArgumentName());
		}
		return converted;
	}

	@Override
	public String getArgumentName() {
		return name;
	}

	@Override
	public String getHelpString() {
		String help = isOptional() ? "\\[" : "<";
		for (int i = 0; i < options.size(); i++) {
			help += (i == 0 ? "" : "|") + convert.toString(options.get(i));
		}
		help += isOptional() ? "\\]" : ">";
		return help;
	}

	@Override
	public String getSpecificationString() {
		String start = isOptional() ? "\\[" : "<";
		String end = isOptional() ? "\\]" : ">";
		return start + getArgumentName() + end;
	}

	@Override
	public List<String> getCompletionSuggestions(ICommandSender sender, String currentInput) {
		List<String> out = new ArrayList<>();
		options.forEach(option -> out.add(convert.toString(option)));

		out.sort((String str1, String str2) -> {
			// See if any string starts with current input
			if (!currentInput.isEmpty() && str1.startsWith(currentInput)) return -1;
			if (!currentInput.isEmpty() && str2.startsWith(currentInput)) return 1;
			// Otherwise, just sort alphabetically
			return str1.compareTo(str2);
		});

		// Make sure that there are tab completions for what user has typed so
		// far
		// If not, don't give any suggestions
		if (!out.get(0).startsWith(currentInput)) {
			return new ArrayList<>();
		}

		return out;
	}

}
