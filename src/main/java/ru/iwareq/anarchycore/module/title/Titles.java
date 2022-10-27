package ru.iwareq.anarchycore.module.title;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum Titles {

	FINES(TitleAPI.Type.DISNEY, "Финес"),
	FERB(TitleAPI.Type.DISNEY, "Ферб"),
	EREN(TitleAPI.Type.ANIME, "Эрен"),
	MIKASSA(TitleAPI.Type.ANIME, "Микасса");

	private static final Map<TitleAPI.Type, Set<Titles>> ALL_BY_TYPE = new HashMap<>();

	static {
		for (TitleAPI.Type type : TitleAPI.Type.values()) {
			ALL_BY_TYPE.put(type, new HashSet<>());
		}

		for (Titles titles : Titles.values()) {
			ALL_BY_TYPE.get(titles.getType()).add(titles);
		}
	}

	private final TitleAPI.Type type;
	private final String name;

	public static Map<TitleAPI.Type, Set<Titles>> getAll() {
		return ALL_BY_TYPE;
	}

	public static Set<Titles> getAllByType(TitleAPI.Type type) {
		return ALL_BY_TYPE.get(type);
	}
}
