run: build
	@java -cp bin forsale.Test
.PHONY: run

build:
	@eclipse -noSplash -data . -application org.eclipse.jdt.apt.core.aptBuild > /dev/null
.PHONY: build
