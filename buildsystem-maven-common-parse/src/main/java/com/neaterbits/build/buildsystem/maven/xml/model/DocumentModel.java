package com.neaterbits.build.buildsystem.maven.xml.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.util.Counter;
import com.neaterbits.util.Indent;
import com.neaterbits.util.Value;
import com.neaterbits.util.parse.context.Context;

public interface DocumentModel<NODE, ELEMENT extends NODE, DOCUMENT extends NODE> {
	
	default <T> void iterate(DOCUMENT document, XMLEventListener<T> eventListener, T param) {
	    iterate(document, eventListener, param, false);
	}

	<T> void iterate(DOCUMENT document, XMLEventListener<T> eventListener, T param, boolean skipIgnoreableWhitespace);

	DOCUMENT createDocument(DOCUMENT other);
	
	ELEMENT getElement(NODE node, String tagName);
	
	ELEMENT addElement(NODE node, String tagName, DOCUMENT document);

	ELEMENT addOrReplaceElement(NODE node, String tagName, DOCUMENT document);

	void addText(ELEMENT element, String text, DOCUMENT document);
	
	String getText(ELEMENT element);
	
	default void printDocument(DOCUMENT document, PrintStream out) {

	    final Integer START_ELEMENT = 1;
        final Integer END_ELEMENT = 2;
        final Integer TEXT_ELEMENT = 3;
	    
	    final Value<Integer> lastType = new Value<>();

	    final Counter counter = new Counter(0);
	    
	    final XMLEventListener<Void> eventListener = new XMLEventListener<Void>() {

            @Override
            public void onStartDocument(Void param) {
                
            }

            @Override
            public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {

                final Integer last = lastType.get();
                if (TEXT_ELEMENT.equals(last)) {
                    throw new IllegalStateException();
                }
                
                final int level = counter.get();

                if (last != null && last.equals(START_ELEMENT)) {
                    out.println();
                }
                
                out.append(Indent.indent(level))
                   .append('<').append(localPart).append('>');

                counter.increase();
                
                lastType.set(START_ELEMENT);
            }

            @Override
            public void onEndElement(Context context, String localPart, Void param) {

                counter.decrease();
                
                if (!lastType.get().equals(TEXT_ELEMENT)) {
                    out.append(Indent.indent(counter.get()));
                }
                
                out.append("</").append(localPart).append('>')
                   .println();
                
                lastType.set(END_ELEMENT);
            }

            @Override
            public void onText(Context context, String data, Void param) {

                out.print(data);
                
                lastType.set(TEXT_ELEMENT);
            }

            @Override
            public void onEndDocument(Void param) {
                
            }
	    };
        
        iterate(document, eventListener, null, true);
	}
	
    default DOCUMENT copyDocument(DOCUMENT toCopy) {
        
        return copyDocument(toCopy, null);
    }

	default DOCUMENT copyDocument(DOCUMENT toCopy, Function<String, String> replaceText) {
		
		Objects.requireNonNull(toCopy);
		
		final DOCUMENT dst = createDocument(toCopy);
		
		final List<ELEMENT> elements = new ArrayList<>();
		
		final XMLEventListener<Void> eventListener = new XMLEventListener<Void>() {

			@Override
			public void onStartDocument(Void param) {

			}

			@Override
			public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {

				final NODE toAddTo = elements.isEmpty()
						? dst
						: elements.get(elements.size() - 1);

				final ELEMENT added = addElement(toAddTo, localPart, dst);
				
				if (added == null) {
					throw new IllegalStateException();
				}
				
				elements.add(added);
			}

			@Override
			public void onText(Context context, String data, Void param) {

				if (elements.isEmpty()) {
					throw new IllegalStateException();
				}

				final String toAdd = replaceText != null
				        ? replaceText.apply(data)
		                : data;
				
				addText(elements.get(elements.size() - 1), toAdd, dst);
			}
			
			@Override
			public void onEndElement(Context context, String localPart, Void param) {

				elements.remove(elements.size() - 1);
				
			}
			
			@Override
			public void onEndDocument(Void param) {
				
			}
		};
		
		iterate(toCopy, eventListener, null, true);
		
		return dst;
	}
}
