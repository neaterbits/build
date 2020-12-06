package com.neaterbits.build.buildsystem.maven.xml.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.XMLReader;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.ImmutableFullContext;

final class JavaxXMLStreamReader implements XMLReader<Void> {

	private final String file;
	private final XMLEventReader xmlEventReader;

	private final boolean skipIgnoreableWhitespace;
	
	JavaxXMLStreamReader(String file, XMLEventReader xmlEventReader, boolean skipIgnoreableWhitespace) {
	
		Objects.requireNonNull(xmlEventReader);
	
		this.file = file;
		this.xmlEventReader = xmlEventReader;
	
		this.skipIgnoreableWhitespace = skipIgnoreableWhitespace;
	}

	@Override
	public <T> Void readXML(XMLEventListener<T> eventListener, T param) throws XMLReaderException {

		try {
	        if (skipIgnoreableWhitespace) {

	            final List<XMLEvent> buffered = new ArrayList<>();
	            
	            // Buffer, then process once we know is not ignoreable whitespace
                while (xmlEventReader.hasNext()) {
                    
                    final XMLEvent xmlEvent = xmlEventReader.nextEvent();
                    
                    if (   xmlEvent.getEventType() == XMLEvent.START_ELEMENT
                        || xmlEvent.getEventType() == XMLEvent.END_ELEMENT
                        || xmlEvent.getEventType() == XMLEvent.CHARACTERS) {

                        buffered.add(xmlEvent);

                        processBuffered(buffered, eventListener, param);
                    }
                }

                // Drain any remaining
                if (!buffered.isEmpty()) {
                    processBuffered(buffered, eventListener, param);
                }

                if (!buffered.isEmpty()) {
                    throw new IllegalStateException(bufferString(buffered));
                }
                
	        }
	        else {
    		
	            // Process directly
	            while (xmlEventReader.hasNext()) {
    				
    				final XMLEvent xmlEvent = xmlEventReader.nextEvent();

    				processXMLEvent(xmlEvent, eventListener, param);
    			}
	        }
		}
		catch (XMLStreamException ex) {
			throw new XMLReaderException("Could not read XML", ex);
		}

		return null;
	}

	private static boolean areOfType(List<XMLEvent> buffered, int startIndex, int endIndex, int xmlEventType) {
	    
	    boolean areOfType = true;
	    
	    for (int i = startIndex; i < endIndex; ++ i) {
	        
	        if (buffered.get(i).getEventType() != xmlEventType) {
	            areOfType = false;
	            break;
	        }
	    }

	    return areOfType;
	}
	
	private <T> void processBuffered(List<XMLEvent> buffered, XMLEventListener<T> eventListener, T param) {

	    if (buffered.size() > 3) {

	        if (   buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                && buffered.get(buffered.size() - 1).getEventType() == XMLEvent.END_ELEMENT
                && areOfType(buffered, 1, buffered.size() - 1, XMLEvent.CHARACTERS)) {

	            for (XMLEvent xmlEvent : buffered) {
	                processXMLEvent(xmlEvent, eventListener, param);
	            }

	            buffered.clear();
	        }
	        else if (   buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                    && areOfType(buffered, 1, buffered.size(), XMLEvent.CHARACTERS)) {
	            
	            // buffer more
	        }
	        else {
	            throw new IllegalStateException(bufferString(buffered));
	        }
	    }
	    else if (buffered.size() == 3) {
	        
	        if (buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
             && buffered.get(1).getEventType() == XMLEvent.CHARACTERS
             && buffered.get(2).getEventType() == XMLEvent.START_ELEMENT) {
	            
	            // Can process first startElement and skip characters
	            processXMLEvent(buffered.get(0), eventListener, param);
	            
	            final XMLEvent remaining = buffered.get(2);

	            buffered.clear();
	            buffered.add(remaining);
	        }
	        else if (buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                  && buffered.get(1).getEventType() == XMLEvent.CHARACTERS
                  && buffered.get(2).getEventType() == XMLEvent.END_ELEMENT) {
	            
	            // <tag>characters</tag>
                processXMLEvent(buffered.get(0), eventListener, param);
                processXMLEvent(buffered.get(1), eventListener, param);
                processXMLEvent(buffered.get(2), eventListener, param);
                
                buffered.clear();
	        }
            else if (buffered.get(0).getEventType() == XMLEvent.END_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.CHARACTERS
                    && buffered.get(2).getEventType() == XMLEvent.END_ELEMENT) {

                throw new IllegalStateException();
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.CHARACTERS
                    && buffered.get(2).getEventType() == XMLEvent.CHARACTERS) {
                
                processXMLEvent(buffered.get(0), eventListener, param);

                processCharElements(
                        (Characters)buffered.get(1),
                        (Characters)buffered.get(2),
                        eventListener, param);

                buffered.clear();
            }
	    }
	    else if (buffered.size() == 2) {
	        
            if (   buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                && buffered.get(1).getEventType() == XMLEvent.START_ELEMENT) {

                processXMLEvent(buffered.get(0), eventListener, param);

                final XMLEvent remaining = buffered.get(1);

                buffered.clear();
                buffered.add(remaining);
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.END_ELEMENT) {
                
                processXMLEvent(buffered.get(0), eventListener, param);
                processXMLEvent(buffered.get(1), eventListener, param);
            
                buffered.clear();
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.END_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.END_ELEMENT) {

                processXMLEvent(buffered.get(0), eventListener, param);

                final XMLEvent remaining = buffered.get(1);

                buffered.clear();
                buffered.add(remaining);

            }
            else if (  buffered.get(0).getEventType() == XMLEvent.END_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.CHARACTERS) {
                
                processXMLEvent(buffered.get(0), eventListener, param);

                buffered.clear();
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.START_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.CHARACTERS) {
                
                // buffer
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.CHARACTERS
                    && buffered.get(1).getEventType() == XMLEvent.CHARACTERS) {
                
                processCharElements(
                        (Characters)buffered.get(0),
                        (Characters)buffered.get(1),
                        eventListener, param);

                buffered.clear();
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.CHARACTERS
                    && buffered.get(1).getEventType() == XMLEvent.START_ELEMENT) {
                
                // Skip characters without processing
                final XMLEvent remaining = buffered.get(1);

                buffered.clear();
                buffered.add(remaining);
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.END_ELEMENT
                    && buffered.get(1).getEventType() == XMLEvent.START_ELEMENT) {
                
                processXMLEvent(buffered.get(0), eventListener, param);
                
                // Skip characters without processing
                final XMLEvent remaining = buffered.get(1);

                buffered.clear();
                buffered.add(remaining);
            }
            else if (  buffered.get(0).getEventType() == XMLEvent.CHARACTERS
                    && buffered.get(1).getEventType() == XMLEvent.END_ELEMENT) {
                
                // Skip characters without processing
                final XMLEvent remaining = buffered.get(1);

                buffered.clear();
                buffered.add(remaining);
            }
            else {
                throw new IllegalStateException("Not handled case " + bufferString(buffered));
            }
	        
	    }
	    else if (buffered.size() == 1) {
	        // Buffer more?
	        if (buffered.get(0).getEventType() == XMLEvent.END_ELEMENT) {
	            processXMLEvent(buffered.get(0), eventListener, param);
	            buffered.clear();
	        }
	    }
	    else {
	        throw new IllegalStateException("Unexpected buffered size " + buffered.size() + "/" + bufferString(buffered));
	    }
	}
	
	private <T> void processCharElements(
	            Characters former,
	            Characters latter,
	            XMLEventListener<T> eventListener,
	            T param) {
	    
	    final String text;
	    
	    if (!former.getData().isBlank() && !latter.getData().isBlank()) {
	        text = former.getData() + latter.getData();
	    }
	    else if (!former.getData().isBlank()) {
	        text = former.getData();
	    }
        else if (!latter.getData().isBlank()) {
            text = latter.getData();
        }
        else {
            text = null;
        }
	    
	    if (text != null) {
            eventListener.onText(
                    context(former, latter),
                    text,
                    null);
	    }
	}
	
	private String bufferString(List<XMLEvent> buffered) {
	    
	    return buffered.stream()
	            .map(JavaxXMLStreamReader::getEventTypeString)
	            .collect(Collectors.toList())
	            .toString();
	}
	
	private static String getEventTypeString(XMLEvent xmlEvent) {
	    
	    final String string;
	    
	    switch (xmlEvent.getEventType()) {
	    
	    case XMLEvent.START_DOCUMENT:
	        string = "START_DOCUMENT";
	        break;
	        
	    case XMLEvent.END_DOCUMENT:
	        string = "END_DOCUMENT";
	        break;
	    
        case XMLEvent.START_ELEMENT:
            string = "START_ELEMENT " + ((StartElement)xmlEvent).getName().getLocalPart();
            break;

        case XMLEvent.END_ELEMENT:
            string = "END_ELEMENT " + ((EndElement)xmlEvent).getName().getLocalPart();
            break;
        
        case XMLEvent.CHARACTERS:
            string = "CHARACTERS '" + ((Characters)xmlEvent).getData() + "'";
            break;
            
        case XMLEvent.ATTRIBUTE:
            string = "ATTRIBUTE '" + ((Attribute)xmlEvent).getValue() + "'";
            break;

        default:
            throw new IllegalStateException("Unknown event type " + xmlEvent.getEventType());
        }

	    return string;
	}
	
	private <T> void processXMLEvent(XMLEvent xmlEvent, XMLEventListener<T> eventListener, T param) {
	    
        switch (xmlEvent.getEventType()) {
        case XMLEvent.START_ELEMENT:
            
            final StartElement startElement = (StartElement)xmlEvent;

            final Iterator<Attribute> attributes = startElement.getAttributes();
            
            final List<XMLAttribute> xmlAttributes;
            
            if (attributes.hasNext()) {
                
                xmlAttributes = new ArrayList<>();
                
                while (attributes.hasNext()) {
                    
                    final Attribute attribute = attributes.next();
                    
                    xmlAttributes.add(new XMLAttribute() {
                        @Override
                        public String getName() {
                            return attribute.getName().getLocalPart();
                        }

                        @Override
                        public String getValue() {
                            return attribute.getValue();
                        }
                    });
                }
            }
            else {
                xmlAttributes = Collections.emptyList();
            }
            
            eventListener.onStartElement(
                    context(startElement),
                    startElement.getName().getLocalPart(),
                    xmlAttributes,
                    param);
            break;
            
        case XMLEvent.END_ELEMENT:
        
            final EndElement endElement = (EndElement)xmlEvent;

            eventListener.onEndElement(
                    context(endElement),
                    endElement.getName().getLocalPart(),
                    param);
            break;
            
        case XMLEvent.CHARACTERS:

            final Characters characters = (Characters)xmlEvent;

            eventListener.onText(
                    context(characters),
                    characters.getData(),
                    param);
            break;
        }
	}

	private Context context(XMLEvent event) {

		return new ImmutableFullContext(
				file,
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				event.getLocation().getLineNumber(),
				event.getLocation().getColumnNumber(),
				event.getLocation().getCharacterOffset(),
				null);

	}

	private Context context(XMLEvent former, XMLEvent latter) {
	    
        return new ImmutableFullContext(
                file,
                former.getLocation().getLineNumber(),
                former.getLocation().getColumnNumber(),
                former.getLocation().getCharacterOffset(),
                latter.getLocation().getLineNumber(),
                latter.getLocation().getColumnNumber(),
                latter.getLocation().getCharacterOffset(),
                null);
	}
}
