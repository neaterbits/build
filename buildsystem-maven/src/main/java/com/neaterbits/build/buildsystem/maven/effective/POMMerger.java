package com.neaterbits.build.buildsystem.maven.effective;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.xml.XMLAttribute;
import com.neaterbits.build.buildsystem.maven.xml.XMLEventListener;
import com.neaterbits.build.buildsystem.maven.xml.model.DocumentModel;
import com.neaterbits.util.parse.context.Context;

final class POMMerger<NODE, ELEMENT extends NODE, DOCUMENT extends NODE> {

	public enum MergeMode {
		NONE,
		ADD,      // List of elements
		MERGE,    // Base element
		REPLACE,
		REPLACE_SUB
	}
	
	@FunctionalInterface
	public interface MergeFilter {
	
		MergeMode merge(List<String> path);
	}
	
	private final DocumentModel<NODE, ELEMENT, DOCUMENT> model;

	POMMerger(DocumentModel<NODE, ELEMENT, DOCUMENT> model) {

		Objects.requireNonNull(model);
		
		this.model = model;
	}
	
	DocumentModel<NODE, ELEMENT, DOCUMENT> getModel() {
		return model;
	}
	
	DOCUMENT merge(DOCUMENT basePom, DOCUMENT subPom, MergeFilter mergeFilter) {
		
		Objects.requireNonNull(basePom);
		Objects.requireNonNull(subPom);
		Objects.requireNonNull(mergeFilter);

		final DOCUMENT merged = model.copyDocument(basePom);
		
		final List<String> path = new ArrayList<>();
		
		// Corresponding path of elements we are merging into
		final List<ELEMENT> mergedElements = new ArrayList<>();
		
		final XMLEventListener<Void> listener = new XMLEventListener<Void>() {
			
			@Override
			public void onStartDocument(Void param) {
				
			}

			@Override
			public void onStartElement(Context context, String localPart, List<XMLAttribute> attributes, Void param) {
				
				final int prevPathSize = path.size();
				
				path.add(localPart);

                // and filter matches
                final MergeMode mergeMode = mergeFilter.merge(path);

                switch (mergeMode) {
                case NONE:
                    // Filter does not match, do nothing
                    break;

                case ADD:
                    if (prevPathSize < mergedElements.size()) {
                        throw new IllegalStateException();
                    }
                    else if (prevPathSize == mergedElements.size()) {

                        final NODE toAddTo = mergedElements.isEmpty()
                                ? merged
                                : mergedElements.get(prevPathSize - 1);

                        final ELEMENT added = model.addElement(toAddTo, localPart, merged);

                        if (added == null) {
                            throw new IllegalStateException();
                        }
                        
                        mergedElements.add(added);
                    }
                    else if (prevPathSize > mergedElements.size()) {
                        throw new IllegalStateException();
                    }
                    break;

                case MERGE: {
                    if (prevPathSize < mergedElements.size()) {
                        throw new IllegalStateException();
                    }
                    else if (prevPathSize == mergedElements.size()) {

                        final NODE toAddTo = mergedElements.isEmpty()
                                ? merged
                                : mergedElements.get(prevPathSize - 1);
                    
                        
                        final ELEMENT existingElement = model.getElement(toAddTo, localPart);
                        
                        final ELEMENT element;

                        if (existingElement == null) {

                            // No root element yet
                            final ELEMENT added = model.addElement(toAddTo, localPart, merged);

                            if (added == null) {
                                throw new IllegalArgumentException();
                            }
                        
                            element = added;
                        }
                        else {
                            element = existingElement;
                        }

                        mergedElements.add(element);
                    }
                    else if (prevPathSize > mergedElements.size()) {

                        throw new IllegalStateException();
                    }
                    break;
                }

				case REPLACE:
                    if (prevPathSize < mergedElements.size()) {
                        throw new IllegalStateException();
                    }
                    else if (prevPathSize == mergedElements.size()) {

                        final ELEMENT toReplaceSubOf = mergedElements.get(prevPathSize - 1);

    					// Replace element from source with that from destination, if exists in subPom
    					
                        final ELEMENT replacedWith = model.addOrReplaceElement(toReplaceSubOf, localPart, merged);

    					if (replacedWith == null) {
    						throw new IllegalStateException();
    					}
    					
    					mergedElements.add(replacedWith);
                    }
                    else {
                        throw new IllegalStateException();
                    }
					break;
					
				case REPLACE_SUB:
					throw new IllegalStateException("Should pass REPLACE here");
					
				default:
					throw new IllegalStateException();
				}
			}
			
			@Override
			public void onText(Context context, String data, Void param) {

				if (mergedElements.size() == path.size()) {
					model.addText(mergedElements.get(mergedElements.size() - 1), data, merged);
				}
			}
			
			@Override
			public void onEndElement(Context context, String localPart, Void param) {

                final int lastIndex = path.size() - 1;
				
				if (!localPart.equals(path.get(lastIndex))) {
					throw new IllegalStateException();
				}
				
				path.remove(lastIndex);
				
				if (lastIndex == mergedElements.size() - 1) {
					mergedElements.remove(lastIndex);
				}
				else if (lastIndex < mergedElements.size() - 1) {
					throw new IllegalStateException();
				}
			}
			
			@Override
			public void onEndDocument(Void param) {
				
			}
		};
		
		model.iterate(subPom, listener, null, true);
		
		return merged;
	}
}
