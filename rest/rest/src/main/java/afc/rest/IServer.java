package afc.rest;

public interface IServer {
	
	String jsonRegionList="{\r\n" + 
			"  \"sequenceNumber\": 123,\r\n" + 
			"  \"regions\": [\r\n" + 
			"    {\r\n" + 
			"      \"resourceId\": \"Weed\",\r\n" + 
			"      \"resultTime\": 1558086914,\r\n" + 
			"      \"locationDimension\": {\r\n" + 
			"         \"latitude\": 37.392529,\r\n" + 
			"         \"longitude\": -5.994072,\r\n" + 
			"         \"width\": 2.38,\r\n" + 
			"         \"length\": 1.25\r\n" + 
			"    }\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}";
	String jsonSchemaRegionList="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"regions\": {\r\n" + 
			"      \"type\": \"array\",\r\n" + 
			"      \"items\": [\r\n" + 
			"        {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"resourceId\": {\r\n" + 
			"              \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"            \"resultTime\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"locationDimension\": {\r\n" + 
			"              \"type\": \"object\",\r\n" + 
			"              \"properties\": {\r\n" + 
			"                \"latitude\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"longitude\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"width\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"length\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                }\r\n" + 
			"              },\r\n" + 
			"              \"required\": [\r\n" + 
			"                \"latitude\",\r\n" + 
			"                \"longitude\",\r\n" + 
			"                \"width\",\r\n" + 
			"                \"length\"\r\n" + 
			"              ]\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"resourceId\",\r\n" + 
			"            \"resultTime\",\r\n" + 
			"            \"locationDimension\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"regions\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonRegion="{\r\n" + 
			"  \"sequenceNumber\": 123,\r\n" + 
			"  \"region\": {\r\n" + 
			"    \"resourceId\": \"Weed\",\r\n" + 
			"    \"resultTime\": 1558086914,\r\n" + 
			"    \"locationDimension\": {\r\n" + 
			"      \"latitude\": 37.392529,\r\n" + 
			"      \"longitude\": -5.994072,\r\n" + 
			"      \"width\": 2.38,\r\n" + 
			"      \"length\": 1.25\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}\r\n" + 
			"";
	String jsonSchemaRegion="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"region\": {\r\n" + 
			"      \"type\": \"object\",\r\n" + 
			"      \"properties\": {\r\n" + 
			"        \"resourceId\": {\r\n" + 
			"          \"type\": \"string\"\r\n" + 
			"        },\r\n" + 
			"        \"resultTime\": {\r\n" + 
			"          \"type\": \"integer\"\r\n" + 
			"        },\r\n" + 
			"        \"locationDimension\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"latitude\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"longitude\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"width\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"length\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"latitude\",\r\n" + 
			"            \"longitude\",\r\n" + 
			"            \"width\",\r\n" + 
			"            \"length\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"resourceId\",\r\n" + 
			"        \"resultTime\",\r\n" + 
			"        \"locationDimension\"\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"region\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonCollarList="{\r\n" + 
			"  \"sequenceNumber\": 123,\r\n" + 
			"  \"collars\": [\r\n" + 
			"    {\r\n" + 
			"      \"resourceId\": \"urn:afc:AS06:livestockObservations:SENSO:collar0034\",\r\n" + 
			"      \"location\": {\r\n" + 
			"        \"latitude\": 45.45123,\r\n" + 
			"        \"longitude\": 25.25456,\r\n" + 
			"        \"altitude\": 2.10789\r\n" + 
			"      },\r\n" + 
			"      \"resultTime\": 1558086914,\r\n" + 
			"      \"resourceAlarm\": true,\r\n" + 
			"      \"anomalies\": {\r\n" + 
			"        \"locationAnomaly\": true,\r\n" + 
			"        \"temperatureAnomaly\": true,\r\n" + 
			"        \"distanceAnomaly\": true,\r\n" + 
			"        \"activityAnomaly\": true,\r\n" + 
			"        \"positionAnomaly\": true\r\n" + 
			"      },\r\n" + 
			"      \"acceleration\": {\r\n" + 
			"        \"accX\": 0,\r\n" + 
			"        \"accY\": 0,\r\n" + 
			"        \"accZ\": 0\r\n" + 
			"      },\r\n" + 
			"      \"temperature\": 36.5\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}\r\n" + 
			"";
	String jsonCollarSchemaList="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"collars\": {\r\n" + 
			"      \"type\": \"array\",\r\n" + 
			"         \"items\": [\r\n" + 
			"           {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"resourceId\": {\r\n" + 
			"            \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"          \"location\": {\r\n" + 
			"            \"type\": \"object\",\r\n" + 
			"            \"properties\": {\r\n" + 
			"               \"latitude\": {\r\n" + 
			"                \"type\": \"number\"\r\n" + 
			"                 },\r\n" + 
			"               \"longitude\": {\r\n" + 
			"               \"type\": \"number\"\r\n" + 
			"               },\r\n" + 
			"               \"altitude\": {\r\n" + 
			"               \"type\": \"number\"\r\n" + 
			"               }\r\n" + 
			"            },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"latitude\",\r\n" + 
			"            \"longitude\",\r\n" + 
			"            \"altitude\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"resultTime\": {\r\n" + 
			"          \"type\": \"integer\"\r\n" + 
			"        },\r\n" + 
			"        \"resourceAlarm\": {\r\n" + 
			"          \"type\": \"boolean\"\r\n" + 
			"        },\r\n" + 
			"        \"anomalies\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"locationAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"temperatureAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"distanceAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"activityAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"positionAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"locationAnomaly\",\r\n" + 
			"            \"temperatureAnomaly\",\r\n" + 
			"            \"distanceAnomaly\",\r\n" + 
			"            \"activityAnomaly\",\r\n" + 
			"            \"positionAnomaly\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"acceleration\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"accX\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"accY\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"accZ\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"accX\",\r\n" + 
			"            \"accY\",\r\n" + 
			"            \"accZ\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"temperature\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"resourceId\",\r\n" + 
			"        \"location\",\r\n" + 
			"        \"resultTime\",\r\n" + 
			"        \"resourceAlarm\",\r\n" + 
			"        \"anomalies\",\r\n" + 
			"        \"acceleration\",\r\n" + 
			"        \"temperature\"\r\n" + 
			"        ]\r\n" + 
			"       }\r\n" + 
		    "      ]\r\n" + 
			"   }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"collars\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonSensorSchemaList="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"resourceId\": {\r\n" + 
			"      \"type\": \"string\"\r\n" + 
			"    },\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"location\": {\r\n" + 
			"      \"type\": \"object\",\r\n" + 
			"      \"properties\": {\r\n" + 
			"        \"latitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        },\r\n" + 
			"        \"longitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        },\r\n" + 
			"        \"altitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"latitude\",\r\n" + 
			"        \"longitude\",\r\n" + 
			"        \"altitude\"\r\n" + 
			"      ]\r\n" + 
			"    },\r\n" + 
			"    \"observations\": {\r\n" + 
			"      \"type\": \"array\",\r\n" + 
			"      \"items\":  {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"resourceId\": {\r\n" + 
			"              \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"            \"observedProperty\": {\r\n" + 
			"              \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"            \"resultTime\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"result\": {\r\n" + 
			"              \"type\": \"object\",\r\n" + 
			"              \"properties\": {\r\n" + 
			"                \"value\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"uom\": {\r\n" + 
			"                  \"type\": \"string\"\r\n" + 
			"                },\r\n" + 
			"                \"variance\": {\r\n" + 
			"                  \"type\": \"integer\"\r\n" + 
			"                }\r\n" + 
			"              },\r\n" + 
			"              \"required\": [\r\n" + 
			"                \"value\",\r\n" + 
			"                \"uom\",\r\n" + 
			"                \"variance\"\r\n" + 
			"              ]\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"resourceId\",\r\n" + 
			"            \"observedProperty\",\r\n" + 
			"            \"resultTime\",\r\n" + 
			"            \"result\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"resourceId\",\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"location\",\r\n" + 
			"    \"observations\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonSensorList="{\"resourceId\":\"urn:afc:AS04:droneMissions:BEV:drone001\""
			+ ",\"sequenceNumber\":123,\"location\":{\"latitude\":45.45123,\"longitude\":25.25456,\"altitude\":2.10789},"
			+ "\"observations\":[{\"resourceId\":\"urn:afc:AS04:environmentalObservations:TST:airTemperatureSensor0012\","
			+ "\"observedProperty\":\"http://environment.data.gov.au/def/property/air_temperature\",\"resultTime\":1558086914,"
			+ "\"result\":{\"value\":3.2,\"uom\":\"http://qudt.org/vocab/unit#DegreeCelsius\",\"variance\":0}}]}";
	/*
	String jsonSensorSchemaList="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"collars\": {\r\n" + 
			"      \"type\": \"array\",\r\n" + 
			"      \"items\": [\r\n" + 
			"        {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"resourceId\": {\r\n" + 
			"              \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"            \"location\": {\r\n" + 
			"              \"type\": \"object\",\r\n" + 
			"              \"properties\": {\r\n" + 
			"                \"latitude\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"longitude\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                },\r\n" + 
			"                \"altitude\": {\r\n" + 
			"                  \"type\": \"number\"\r\n" + 
			"                }\r\n" + 
			"              },\r\n" + 
			"              \"required\": [\r\n" + 
			"                \"latitude\",\r\n" + 
			"                \"longitude\",\r\n" + 
			"                \"altitude\"\r\n" + 
			"              ]\r\n" + 
			"            },\r\n" + 
			"            \"resultTime\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"resourceAlarm\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"anomalies\": {\r\n" + 
			"              \"type\": \"object\",\r\n" + 
			"              \"properties\": {\r\n" + 
			"                \"locationAnomaly\": {\r\n" + 
			"                  \"type\": \"boolean\"\r\n" + 
			"                },\r\n" + 
			"                \"temperatureAnomaly\": {\r\n" + 
			"                  \"type\": \"boolean\"\r\n" + 
			"                },\r\n" + 
			"                \"distanceAnomaly\": {\r\n" + 
			"                  \"type\": \"boolean\"\r\n" + 
			"                },\r\n" + 
			"                \"activityAnomaly\": {\r\n" + 
			"                  \"type\": \"boolean\"\r\n" + 
			"                },\r\n" + 
			"                \"positionAnomaly\": {\r\n" + 
			"                  \"type\": \"boolean\"\r\n" + 
			"                }\r\n" + 
			"              },\r\n" + 
			"              \"required\": [\r\n" + 
			"                \"locationAnomaly\",\r\n" + 
			"                \"temperatureAnomaly\",\r\n" + 
			"                \"distanceAnomaly\",\r\n" + 
			"                \"activityAnomaly\",\r\n" + 
			"                \"positionAnomaly\"\r\n" + 
			"              ]\r\n" + 
			"            },\r\n" + 
			"            \"acceleration\": {\r\n" + 
			"              \"type\": \"object\",\r\n" + 
			"              \"properties\": {\r\n" + 
			"                \"accX\": {\r\n" + 
			"                  \"type\": \"integer\"\r\n" + 
			"                },\r\n" + 
			"                \"accY\": {\r\n" + 
			"                  \"type\": \"integer\"\r\n" + 
			"                },\r\n" + 
			"                \"accZ\": {\r\n" + 
			"                  \"type\": \"integer\"\r\n" + 
			"                }\r\n" + 
			"              },\r\n" + 
			"              \"required\": [\r\n" + 
			"                \"accX\",\r\n" + 
			"                \"accY\",\r\n" + 
			"                \"accZ\"\r\n" + 
			"              ]\r\n" + 
			"            },\r\n" + 
			"            \"temperature\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"resourceId\",\r\n" + 
			"            \"location\",\r\n" + 
			"            \"resultTime\",\r\n" + 
			"            \"resourceAlarm\",\r\n" + 
			"            \"anomalies\",\r\n" + 
			"            \"acceleration\",\r\n" + 
			"            \"temperature\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"collars\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonSensorList="{\r\n" + 
			"  \"sequenceNumber\": 123,\r\n" + 
			"  \"collars\": [\r\n" + 
			"    {\r\n" + 
			"      \"resourceId\": \"urn:afc:AS06:livestockObservations:SENSO:collar0034\",\r\n" + 
			"      \"location\": {\r\n" + 
			"        \"latitude\": 45.45123,\r\n" + 
			"        \"longitude\": 25.25456,\r\n" + 
			"        \"altitude\": 2.10789\r\n" + 
			"      },\r\n" + 
			"      \"resultTime\": 1558086914,\r\n" + 
			"      \"resourceAlarm\": true,\r\n" + 
			"      \"anomalies\": {\r\n" + 
			"        \"locationAnomaly\": true,\r\n" + 
			"        \"temperatureAnomaly\": true,\r\n" + 
			"        \"distanceAnomaly\": true,\r\n" + 
			"        \"activityAnomaly\": true,\r\n" + 
			"        \"positionAnomaly\": true\r\n" + 
			"      },\r\n" + 
			"      \"acceleration\": {\r\n" + 
			"        \"accX\": 0,\r\n" + 
			"        \"accY\": 0,\r\n" + 
			"        \"accZ\": 0\r\n" + 
			"      },\r\n" + 
			"      \"temperature\": 36.5\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}\r\n" + 
			"";
			*/
	
	String jsonSensorSchema="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"location\": {\r\n" + 
			"      \"type\": \"object\",\r\n" + 
			"      \"properties\": {\r\n" + 
			"        \"latitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        },\r\n" + 
			"        \"longitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        },\r\n" + 
			"        \"altitude\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"latitude\",\r\n" + 
			"        \"longitude\",\r\n" + 
			"        \"altitude\"\r\n" + 
			"      ]\r\n" + 
			"    },\r\n" + 
			"    \"observation\": {\r\n" + 
			"      \"type\": \"object\",\r\n" + 
			"      \"properties\": {\r\n" + 
			"        \"resourceId\": {\r\n" + 
			"          \"type\": \"string\"\r\n" + 
			"        },\r\n" + 
			"        \"observedProperty\": {\r\n" + 
			"          \"type\": \"string\"\r\n" + 
			"        },\r\n" + 
			"        \"resultTime\": {\r\n" + 
			"          \"type\": \"integer\"\r\n" + 
			"        },\r\n" + 
			"        \"result\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"value\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"uom\": {\r\n" + 
			"              \"type\": \"string\"\r\n" + 
			"            },\r\n" + 
			"            \"variance\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"value\",\r\n" + 
			"            \"uom\",\r\n" + 
			"            \"variance\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"resourceId\",\r\n" + 
			"        \"observedProperty\",\r\n" + 
			"        \"resultTime\",\r\n" + 
			"        \"result\"\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"location\",\r\n" + 
			"    \"observation\"\r\n" + 
			"  ]\r\n" + 
			"}";
	
	String jsonSensor="{\r\n" + 
			"  \"sequenceNumber\": 123,\r\n" + 
			"  \"location\": {\r\n" + 
			"    \"latitude\": 45.45123,\r\n" + 
			"    \"longitude\": 25.25456,\r\n" + 
			"    \"altitude\": 2.10789\r\n" + 
			"  },\r\n" + 
			"  \"observation\": {\r\n" + 
			"    \"resourceId\": \"urn:afc:AS04:environmentalObservations:TST:airTemperatureSensor0012\",\r\n" + 
			"    \"observedProperty\": \"http://environment.data.gov.au/def/property/air_temperature\",\r\n" + 
			"    \"resultTime\": 1558086914,\r\n" + 
			"    \"result\": {\r\n" + 
			"      \"value\": 3.2,\r\n" + 
			"      \"uom\": \"http://qudt.org/vocab/unit#DegreeCelsius\",\r\n" + 
			"      \"variance\": 0\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}\r\n" + 
			"";
	
	String jsonCollarSchema="{\r\n" + 
			"  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\r\n" + 
			"  \"type\": \"object\",\r\n" + 
			"  \"properties\": {\r\n" + 
			"    \"sequenceNumber\": {\r\n" + 
			"      \"type\": \"integer\"\r\n" + 
			"    },\r\n" + 
			"    \"collar\": {\r\n" + 
			"      \"type\": \"object\",\r\n" + 
			"      \"properties\": {\r\n" + 
			"        \"resourceId\": {\r\n" + 
			"          \"type\": \"string\"\r\n" + 
			"        },\r\n" + 
			"        \"location\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"latitude\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"longitude\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            },\r\n" + 
			"            \"altitude\": {\r\n" + 
			"              \"type\": \"number\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"latitude\",\r\n" + 
			"            \"longitude\",\r\n" + 
			"            \"altitude\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"resultTime\": {\r\n" + 
			"          \"type\": \"integer\"\r\n" + 
			"        },\r\n" + 
			"        \"resourceAlarm\": {\r\n" + 
			"          \"type\": \"boolean\"\r\n" + 
			"        },\r\n" + 
			"        \"anomalies\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"locationAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"temperatureAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"distanceAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"activityAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            },\r\n" + 
			"            \"positionAnomaly\": {\r\n" + 
			"              \"type\": \"boolean\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"locationAnomaly\",\r\n" + 
			"            \"temperatureAnomaly\",\r\n" + 
			"            \"distanceAnomaly\",\r\n" + 
			"            \"activityAnomaly\",\r\n" + 
			"            \"positionAnomaly\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"acceleration\": {\r\n" + 
			"          \"type\": \"object\",\r\n" + 
			"          \"properties\": {\r\n" + 
			"            \"accX\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"accY\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            },\r\n" + 
			"            \"accZ\": {\r\n" + 
			"              \"type\": \"integer\"\r\n" + 
			"            }\r\n" + 
			"          },\r\n" + 
			"          \"required\": [\r\n" + 
			"            \"accX\",\r\n" + 
			"            \"accY\",\r\n" + 
			"            \"accZ\"\r\n" + 
			"          ]\r\n" + 
			"        },\r\n" + 
			"        \"temperature\": {\r\n" + 
			"          \"type\": \"number\"\r\n" + 
			"        }\r\n" + 
			"      },\r\n" + 
			"      \"required\": [\r\n" + 
			"        \"resourceId\",\r\n" + 
			"        \"location\",\r\n" + 
			"        \"resultTime\",\r\n" + 
			"        \"resourceAlarm\",\r\n" + 
			"        \"anomalies\",\r\n" + 
			"        \"acceleration\",\r\n" + 
			"        \"temperature\"\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"required\": [\r\n" + 
			"    \"sequenceNumber\",\r\n" + 
			"    \"collar\"\r\n" + 
			"  ]\r\n" + 
			"}";
	String jsonCollar="{"
			  +"\"sequenceNumber\": 123,"
			  +"\"collar\": {"
			    +"\"resourceId\": \"urn:afc:AS06:livestockObservations:SENSO:collar0034\","
			    +"\"location\": {"
			     +"\"latitude\": 45.45123,"
			      +"\"longitude\": 25.25456,"
			      +"\"altitude\": 2.10789"
			    +"},"
			    +"\"resultTime\": 1558086914,"
			    +"\"resourceAlarm\": true,"
			    +"\"anomalies\": {"
			     +"\"locationAnomaly\": true,"
			      +"\"temperatureAnomaly\": true,"
			      +"\"distanceAnomaly\": true,"
			      +"\"activityAnomaly\": true,"
			      +"\"positionAnomaly\": true"
			    +"},"
			    +"\"acceleration\": {"
			      +"\"accX\": 0,"
			      +"\"accY\": 0,"
			      +"\"accZ\": 0"
			    +"},"
			    +"\"temperature\": 36.5"
			  +"}"
			+"}";

}
