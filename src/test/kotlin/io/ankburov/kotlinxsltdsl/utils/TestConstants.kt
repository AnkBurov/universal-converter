package io.ankburov.kotlinxsltdsl.utils

class TestConstants {

    companion object {
        val EXPECTED_XML = """<?xml version="1.0" encoding="UTF-8"?>
<root xmlns="some">
	<cars>
		<year>
			1997
		</year>
		<brand>
			Ford
		</brand>
		<model>
			E350
		</model>
		<something isWhatever="sure">
			<nested>
				nested
			</nested>
		</something>
	</cars>
	<cars>
		<year>
			2012
		</year>
		<brand>
			Tesla
		</brand>
		<model>
			Model S
		</model>
		<something isWhatever="sure">
			<nested>
				nested
			</nested>
		</something>
	</cars>
</root>""".trimIndent()
    }
}