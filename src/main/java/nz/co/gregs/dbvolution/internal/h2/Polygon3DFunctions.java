/*
 * Copyright 2015 gregorygraham.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.co.gregs.dbvolution.internal.h2;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author gregorygraham
 */
public enum Polygon3DFunctions implements DBVFeature {

	/**
	 *
	 */
	CREATE_FROM_WKTPOLYGON3D("String", "String wkt", "return wkt;"),

	/**
	 *
	 */
	CREATE_FROM_POINT3DS("String", "String... pointsArray", ""
			+ "try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				GeometryFactory factory = new GeometryFactory();\n"
			+ "				List<Coordinate> coords = new ArrayList<Coordinate>();\n"
			+ "				String originalStr;\n"
			+ "				int numberOfPoints = pointsArray.length;\n"
			+ "				for (int index = 0; index < numberOfPoints; index++) {\n"
			+ "					originalStr = pointsArray[index];\n"
			+ "					if (originalStr == null) {\n"
			+ "						return null;\n"
			+ "					} else {\n"
			+ "						Point point = null;\n"
			+ "						Geometry geometry;\n"
			+ "						geometry = wktReader.read(originalStr);\n"
			+ "						if (geometry instanceof Point) {\n"
			+ "							point = (Point) geometry;\n"
			+ "							coords.add(point.getCoordinate());\n"
			+ "						} else {\n"
			+ "							throw new RuntimeException(\"Failed To Parse H2 Polygon3D\");\n"
			+ "						}\n"
			+ "					}\n"
			+ "				}\n"
			+ "				Polygon createPolygon = factory.createPolygon(coords.toArray(new Coordinate[]{}));\n"
			+ "				createPolygon.normalize();\n"
			+ "				return createPolygon.toText();\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse H2 Polygon3D\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	EQUALS("Boolean", "String firstPolyStr, String secondPolyStr", "\n"
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry;\n"
			+ "					Geometry secondGeometry;\n"
			+ "					firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						firstPoly.normalize();\n"
			+ "						secondPoly.normalize();\n"
			+ "						return firstPoly.toText().equals(secondPoly.toText());\n"
			+ "					} else {\n"
			+ "						return false;"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon: either '\"+firstPolyStr+\"' or '\"+secondPolyStr+\"' is wrong somehow\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	AREA("Double", "String firstPolyStr", "\n"
			+ "			try {\n"
			+ "				if (firstPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry;\n"
			+ "					firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						return firstPoly.getArea();\n"
			+ "					} else {\n"
			+ "						return null;"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon: '\"+firstPolyStr+\"'\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	MEASURABLE_DIMENSIONS("Integer", "String firstPoly", "return 2;"),

	/**
	 *
	 */
	MIN_Z("Double", "String firstPoly", ""
			+ "			try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					Polygon polygon;\n"
			+ "					Geometry geometry;\n"
			+ "					geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						polygon = (Polygon) geometry;\n"
			+ "						Double minZ = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (minZ == null || coordinate.z < minZ) {\n"
			+ "								minZ = coordinate.z;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return minZ;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	MAX_Z("Double", "String firstPoly", ""
			+ "			try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					Geometry geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						Polygon polygon = (Polygon) geometry;\n"
			+ "						Double maxZ = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (maxZ == null || coordinate.z > maxZ) {\n"
			+ "								maxZ = coordinate.y;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return maxZ;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),


	/**
	 *
	 */
	MIN_Y("Double", "String firstPoly", ""
			+ "			try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					Polygon polygon;\n"
			+ "					Geometry geometry;\n"
			+ "					geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						polygon = (Polygon) geometry;\n"
			+ "						Double minY = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (minY == null || coordinate.y < minY) {\n"
			+ "								minY = coordinate.y;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return minY;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	MAX_Y("Double", "String firstPoly", ""
			+ "			try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					Geometry geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						Polygon polygon = (Polygon) geometry;\n"
			+ "						Double maxY = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (maxY == null || coordinate.y > maxY) {\n"
			+ "								maxY = coordinate.y;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return maxY;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	MAX_X("Double", "String firstPoly", ""
			+ "			try {\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					Geometry geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						Polygon polygon = (Polygon) geometry;\n"
			+ "						Double maxX = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (maxX == null || coordinate.x > maxX) {\n"
			+ "								maxX = coordinate.x;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return maxX;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	MIN_X("Double", "String firstPoly", "" + "			try {\n"
			+ "				WKTReader wktReader = new WKTReader();\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					Geometry geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						Polygon polygon = (Polygon) geometry;\n"
			+ "						Double minX = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (minX == null || coordinate.x < minX) {\n"
			+ "								minX = coordinate.x;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						return minX;\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	BOUNDINGBOX("String", "String firstPoly", "\n"
			+ "			try {\n"
			+ "				if (firstPoly == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry geometry = wktReader.read(firstPoly);\n"
			+ "					if (geometry instanceof Polygon) {\n"
			+ "						Polygon polygon = (Polygon) geometry;\n"
			+ "						Double minX = null;\n"
			+ "						Double minY = null;\n"
			+ "						Double minZ = null;\n"
			+ "						Double maxX = null;\n"
			+ "						Double maxY = null;\n"
			+ "						Double maxZ = null;\n"
			+ "						Coordinate[] coordinates = polygon.getCoordinates();\n"
			+ "						for (Coordinate coordinate : coordinates) {\n"
			+ "							if (minX == null || coordinate.x < minX) {\n"
			+ "								minX = coordinate.x;\n"
			+ "							}\n"
			+ "							if (minY == null || coordinate.y < minY) {\n"
			+ "								minY = coordinate.y;\n"
			+ "							}\n"
			+ "							if (minZ == null || coordinate.z < minZ) {\n"
			+ "								minZ = coordinate.z;\n"
			+ "							}\n"
			+ "							if (maxX == null || coordinate.x > maxX) {\n"
			+ "								maxX = coordinate.x;\n"
			+ "							}\n"
			+ "							if (maxY == null || coordinate.y > minY) {\n"
			+ "								maxY = coordinate.y;\n"
			+ "							}\n"
			+ "							if (maxZ == null || coordinate.z > minZ) {\n"
			+ "								maxZ = coordinate.z;\n"
			+ "							}\n"
			+ "						}\n"
			+ "						Polygon createPolygon = factory.createPolygon(new Coordinate[]{\n"
			+ "							new Coordinate(minX, minY, minZ),\n"
			+ "							new Coordinate(maxX, minY, minZ),\n"
			+ "							new Coordinate(maxX, maxY, minZ),\n"
			+ "							new Coordinate(maxX, maxY, maxZ),\n"
			+ "							new Coordinate(minX, maxY, maxZ),\n"
			+ "							new Coordinate(minX, minY, maxZ),\n"
			+ "							new Coordinate(minX, minY, minZ),});\n"
			+ "						createPolygon.normalize();\n"
			+ "						return createPolygon.toText();\n"
			+ "					} else {\n"
			+ "						throw new RuntimeException(\"Failed To Parse Polygon\");\n"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}\n"),

	/**
	 *
	 */
	TOUCHES("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.touches(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}\n"
			+ ""),

	/**
	 *
	 */
	EXTERIORRING("String", "String firstPolyStr", "\n"
			+ "			try {\n"
			+ "				if (firstPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory3D factory = new GeometryFactory3D();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)) {\n"
			+ "						PolygonZ firstPoly = factory.createPolygonZ((Polygon) firstGeometry);\n"
			+ "						final LinearRingZ exteriorRing = firstPoly.getExteriorRingZ();\n"
			+ "						exteriorRing.normalize();\n"
			+ "						LineStringZ createLineString = factory.createLineStringZ(exteriorRing.getCoordinateSequence());\n"
			+ "						Geometry reverse = createLineString.reverse();\n"
			+ "						return reverse.toText();\n"
			+ "					} else {\n"
			+ "						return null;"
			+ "					}\n"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse SQLite Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	CONTAINS_POLYGON3D("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.contains(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	CONTAINS_POINT3D("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Point)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Point secondPoly = (Point) secondGeometry;\n"
			+ "						return firstPoly.contains(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	WITHIN("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.within(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	OVERLAPS("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.overlaps(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	INTERSECTS("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.intersects(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}"),

	/**
	 *
	 */
	DISJOINT("Boolean", "String firstPolyStr, String secondPolyStr", ""
			+ "			try {\n"
			+ "				if (firstPolyStr == null || secondPolyStr == null) {\n"
			+ "					return null;\n"
			+ "				} else {\n"
			+ "					WKTReader wktReader = new WKTReader();\n"
			+ "					GeometryFactory factory = new GeometryFactory();\n"
			+ "					Geometry firstGeometry = wktReader.read(firstPolyStr);\n"
			+ "					Geometry secondGeometry = wktReader.read(secondPolyStr);\n"
			+ "					if ((firstGeometry instanceof Polygon)&&(secondGeometry instanceof Polygon)) {\n"
			+ "						Polygon firstPoly = (Polygon) firstGeometry;\n"
			+ "						Polygon secondPoly = (Polygon) secondGeometry;\n"
			+ "						return firstPoly.disjoint(secondPoly);\n"
			+ "					}else{"
			+ "						return false;"
			+ "					}"
			+ "				}\n"
			+ "			} catch (Exception ex) {\n"
			+ "				throw new RuntimeException(\"Failed To Parse Polygon\", ex);\n"
			+ "			}");

	private final String returnType;
	private final String parameters;
	private final String code;

	Polygon3DFunctions(String returnType, String parameters, String code) {
		this.returnType = returnType;
		this.parameters = parameters;
		this.code = code;
	}

	@Override
	public String toString(){
		return alias();
	}

	@Override
	public String alias() {
		return "DBV_POLYGON3D_" + name();
	}

	/**
	 *
	 * @param stmt
	 * @throws SQLException
	 */
	@Override
	public void add(Statement stmt) throws SQLException {
		try {
			stmt.execute("DROP ALIAS " + alias() + ";");
		} catch (SQLException sqlex) {
			;
		}
		if (code.isEmpty()) {
			stmt.execute("CREATE ALIAS IF NOT EXISTS " + alias() + " DETERMINISTIC AS $$ \n"
					+ "import com.vividsolutions.jts.geom.*; import com.vividsolutions.jts.io.*;\n import java.util.*;\n" + "@CODE " + returnType + " " + alias() + "(" + parameters + ") {\n throw new UnsupportedOperationException(\"Not supported yet.\");} $$;");
		} else {
			stmt.execute("CREATE ALIAS IF NOT EXISTS " + alias() + " DETERMINISTIC AS $$ \n"
					+ "import com.vividsolutions.jts.geom.*;import nz.co.gregs.dbvolution.datatypes.spatial3D.*;import com.vividsolutions.jts.geom.*; import com.vividsolutions.jts.io.*;\n import java.util.*;\n" + "@CODE " + returnType + " " + alias() + "(" + parameters + ") {\n" + code + "} $$;");
		}
	}
}
