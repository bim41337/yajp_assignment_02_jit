package de.oth.jit;

import java.io.Serializable;

interface JitObject extends Serializable {
	
	// hash method here
	// getHashKey liefert Hashwerte eines Objekts
	// getObjectContent liefert Inhalt der Datei im Commit
	// Wie Ordnerstruktur aufbauen? JitCommit mit Kindliste anstatt StageController?
	//	Kindliste eines Commits wären dann alle Ordner, die direkt im Workspace-Dir. liegen
	//	Einfach durchgehen und Match suchen? => equals für JitDir / JitFile anhand Pfadstring?
	// JitCommit erstellt über Constructor anhand eines StagingControllers die Struktur per Iterator<Path> ...
	
}
